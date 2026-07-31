package com.findingjob.profile.controller;

import com.findingjob.common.dto.ApiResponse;
import com.findingjob.common.enums.UserRole;
import com.findingjob.common.security.JwtUserPrincipal;
import com.findingjob.profile.dto.JobseekerProfileDto;
import com.findingjob.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Jobseeker Profile", description = "Profile CRUD and public view")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get or create my profile")
    public ApiResponse<JobseekerProfileDto> getMyProfile(@AuthenticationPrincipal JwtUserPrincipal principal) {
        String name = principal.getPhone() != null ? principal.getPhone() : "用户";
        return ApiResponse.success(profileService.getOrCreateProfile(principal.getUserId(), name));
    }

    @PutMapping("/me")
    @Operation(summary = "Update my profile")
    public ApiResponse<JobseekerProfileDto> updateMyProfile(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody JobseekerProfileDto dto) {
        return ApiResponse.success(profileService.updateProfile(principal.getUserId(), dto));
    }

    @GetMapping("/public/{userId}")
    @Operation(summary = "Get public profile info (L1, anonymized)")
    public ApiResponse<JobseekerProfileDto> getPublicProfile(@PathVariable Long userId) {
        return ApiResponse.success(profileService.getPublicProfile(userId));
    }
}
