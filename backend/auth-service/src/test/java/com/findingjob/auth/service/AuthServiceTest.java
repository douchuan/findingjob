package com.findingjob.auth.service;

import com.findingjob.auth.dto.LoginResponse;
import com.findingjob.auth.dto.PhoneLoginRequest;
import com.findingjob.auth.dto.RoleSelectionRequest;
import com.findingjob.auth.entity.User;
import com.findingjob.auth.repository.UserRepository;
import com.findingjob.common.enums.UserRole;
import com.findingjob.common.enums.UserStatus;
import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil() {
            @Override
            public String generateToken(Long userId, UserRole role, String phone) {
                return "mock-token-" + userId + "-" + role.name();
            }
        };
        authService = new AuthService(userRepository, jwtUtil);
    }

    @Test
    void handleOAuthLogin_newUser_returnsNeedsRole() {
        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("testuser");
        when(userRepository.findByGithubId("mock-id")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        LoginResponse response = authService.handleOAuthLogin("github", "mock-id", "testuser", "");

        assertFalse(response.isRoleSelected());
        assertNull(response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void handleOAuthLogin_existingUserWithRole_returnsToken() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("testuser");
        existingUser.setRole(UserRole.JOBSEEKER);
        existingUser.setPhone("13800138000");
        when(userRepository.findByGithubId("mock-id")).thenReturn(Optional.of(existingUser));

        LoginResponse response = authService.handleOAuthLogin("github", "mock-id", "testuser", "");

        assertTrue(response.isRoleSelected());
        assertEquals("mock-token-1-JOBSEEKER", response.getToken());
        assertEquals("JOBSEEKER", response.getRole());
    }

    @Test
    void handlePhoneLogin_newUser_createsUser() {
        PhoneLoginRequest request = new PhoneLoginRequest();
        request.setPhone("13800138000");
        request.setCode("123456");

        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        LoginResponse response = authService.handlePhoneLogin(request);

        assertFalse(response.isRoleSelected());
        assertEquals(1L, response.getUserId());
    }

    @Test
    void handlePhoneLogin_invalidCode_throwsException() {
        PhoneLoginRequest request = new PhoneLoginRequest();
        request.setPhone("13800138000");
        request.setCode("123");

        assertThrows(BusinessException.class, () -> authService.handlePhoneLogin(request));
    }

    @Test
    void handlePhoneLogin_shortCode_throwsException() {
        PhoneLoginRequest request = new PhoneLoginRequest();
        request.setPhone("13800138000");
        request.setCode("12345");

        assertThrows(BusinessException.class, () -> authService.handlePhoneLogin(request));
    }

    @Test
    void selectRole_success() {
        RoleSelectionRequest request = new RoleSelectionRequest();
        request.setRole("JOBSEEKER");

        User user = new User();
        user.setId(1L);
        user.setName("testuser");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoginResponse response = authService.selectRole(1L, request);

        assertTrue(response.isRoleSelected());
        assertEquals("JOBSEEKER", response.getRole());
        assertTrue(response.getToken().contains("JOBSEEKER"));
        verify(userRepository).save(argThat(u -> u.getRole() == UserRole.JOBSEEKER));
    }

    @Test
    void selectRole_alreadySelected_throwsException() {
        RoleSelectionRequest request = new RoleSelectionRequest();
        request.setRole("HR");

        User user = new User();
        user.setId(1L);
        user.setRole(UserRole.JOBSEEKER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class, () -> authService.selectRole(1L, request));
    }

    @Test
    void selectRole_invalidRole_throwsException() {
        RoleSelectionRequest request = new RoleSelectionRequest();
        request.setRole("INVALID");

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class, () -> authService.selectRole(1L, request));
    }
}
