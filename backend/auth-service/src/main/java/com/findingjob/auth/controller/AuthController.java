package com.findingjob.auth.controller;

import com.findingjob.auth.dto.DevLoginRequest;
import com.findingjob.auth.dto.LoginResponse;
import com.findingjob.auth.dto.PhoneLoginRequest;
import com.findingjob.auth.dto.RoleSelectionRequest;
import com.findingjob.auth.entity.User;
import com.findingjob.auth.repository.UserRepository;
import com.findingjob.auth.service.AccountDeletionService;
import com.findingjob.auth.service.AuthService;
import com.findingjob.auth.service.StatsService;
import com.findingjob.common.dto.ApiResponse;
import com.findingjob.common.enums.UserRole;
import com.findingjob.common.enums.UserStatus;
import com.findingjob.common.security.JwtUserPrincipal;
import com.findingjob.common.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Login, OAuth, and role selection")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final StatsService statsService;
    private final AccountDeletionService accountDeletionService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, UserRepository userRepository,
                          StatsService statsService, AccountDeletionService accountDeletionService,
                          JwtUtil jwtUtil) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.statsService = statsService;
        this.accountDeletionService = accountDeletionService;
        this.jwtUtil = jwtUtil;
    }

    // === Dev-only: direct login for testing ===

    @PostMapping("/dev-login")
    @Profile("dev")
    @Operation(summary = "Dev login — bypasses OAuth and SMS (dev profile only)")
    public ApiResponse<LoginResponse> devLogin(@RequestBody DevLoginRequest request) {
        UserRole role = UserRole.valueOf(request.getRole().toUpperCase());

        // Find or create user
        User user = userRepository.findByName(request.getName())
                .orElseGet(() -> {
                    User u = new User();
                    u.setName(request.getName());
                    u.setRole(role);
                    u.setStatus(UserStatus.ACTIVE);
                    return userRepository.save(u);
                });

        // If role wasn't set (existing user from role selection flow), set it
        if (user.getRole() == null) {
            user.setRole(role);
            userRepository.save(user);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole(), user.getPhone());
        return ApiResponse.success(new LoginResponse(
                token, true, role.name(), user.getId(), user.getName(), user.getAvatar()
        ));
    }

    // === Production endpoints ===

    @GetMapping("/oauth/{provider}/authorize")
    @Operation(summary = "Get OAuth authorize URL")
    public ApiResponse<String> getAuthorizeUrl(@PathVariable String provider) {
        String authUrl = "https://github.com/login/oauth/authorize?client_id=CLIENT_ID&redirect_uri=http://localhost:8001/api/auth/oauth/" + provider + "/callback&scope=read:user";
        return ApiResponse.success(authUrl);
    }

    @GetMapping("/oauth/{provider}/callback")
    @Operation(summary = "OAuth callback handler")
    public void oauthCallback(
            @PathVariable String provider,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {

        String mockUserId = "mock-" + provider + "-" + System.currentTimeMillis();
        String mockUsername = "user_" + provider;
        String mockAvatar = "";

        LoginResponse result = authService.handleOAuthLogin(provider, mockUserId, mockUsername, mockAvatar);

        if (result.isRoleSelected()) {
            response.sendRedirect("/?token=" + result.getToken() + "&userId=" + result.getUserId());
        } else {
            response.sendRedirect("/select-role?userId=" + result.getUserId() + "&name=" + result.getName());
        }
    }

    @PostMapping("/phone-login")
    @Operation(summary = "Login with phone number and SMS code (MVP: mock code)")
    public ApiResponse<LoginResponse> phoneLogin(@Valid @RequestBody PhoneLoginRequest request) {
        return ApiResponse.success(authService.handlePhoneLogin(request));
    }

    @PostMapping("/select-role")
    @Operation(summary = "Select role for first-time login")
    public ApiResponse<LoginResponse> selectRole(
            @RequestHeader(value = "X-User-Id") Long userId,
            @Valid @RequestBody RoleSelectionRequest request) {
        return ApiResponse.success(authService.selectRole(userId, request));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user info")
    public ApiResponse<Map<String, Object>> me(@AuthenticationPrincipal JwtUserPrincipal principal) {
        User user = userRepository.findById(principal.getUserId()).orElse(null);

        if (user == null) {
            return ApiResponse.error(404, "User not found");
        }

        return ApiResponse.success(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "avatar", user.getAvatar(),
                "phone", user.getPhone(),
                "role", user.getRole() != null ? user.getRole().name() : null,
                "status", user.getStatus().name()
        ));
    }

    @GetMapping("/admin/stats")
    @Operation(summary = "Get platform stats (admin only)")
    public ApiResponse<Map<String, Object>> getPlatformStats() {
        return ApiResponse.success(statsService.getPlatformStats());
    }

    @PostMapping("/me/delete")
    @Operation(summary = "Request account deletion (7-day cooling)")
    public ApiResponse<Map<String, Object>> requestDeletion(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(accountDeletionService.requestDeletion(principal.getUserId()));
    }

    @PostMapping("/me/delete/cancel")
    @Operation(summary = "Cancel pending account deletion")
    public ApiResponse<Map<String, Object>> cancelDeletion(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(accountDeletionService.cancelDeletion(principal.getUserId()));
    }
}
