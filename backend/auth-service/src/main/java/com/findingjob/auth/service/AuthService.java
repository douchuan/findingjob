package com.findingjob.auth.service;

import com.findingjob.auth.dto.LoginResponse;
import com.findingjob.auth.dto.PhoneLoginRequest;
import com.findingjob.auth.dto.RoleSelectionRequest;
import com.findingjob.auth.entity.User;
import com.findingjob.auth.repository.UserRepository;
import com.findingjob.common.enums.UserRole;
import com.findingjob.common.enums.UserStatus;
import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import com.findingjob.common.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public LoginResponse handleOAuthLogin(String provider, String providerId, String username, String avatar) {
        User user = findOrCreateByProvider(provider, providerId, username, avatar);

        if (user.getRole() == null) {
            return new LoginResponse(null, false, null, user.getId(), user.getName(), user.getAvatar());
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole(), user.getPhone());
        return new LoginResponse(token, true, user.getRole().name(), user.getId(), user.getName(), user.getAvatar());
    }

    @Transactional
    public LoginResponse handlePhoneLogin(PhoneLoginRequest request) {
        if (request.getCode() == null || request.getCode().length() != 6) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "验证码必须是6位数字");
        }

        User user = userRepository.findByPhone(request.getPhone())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setPhone(request.getPhone());
                    newUser.setStatus(UserStatus.ACTIVE);
                    return userRepository.save(newUser);
                });

        if (user.getRole() == null) {
            return new LoginResponse(null, false, null, user.getId(), user.getName(), user.getAvatar());
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole(), user.getPhone());
        return new LoginResponse(token, true, user.getRole().name(), user.getId(), user.getName(), user.getAvatar());
    }

    @Transactional
    public LoginResponse selectRole(Long userId, RoleSelectionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getRole() != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_ENTRY, "角色已选择，不可更改");
        }

        UserRole role;
        try {
            role = UserRole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "无效的角色值");
        }

        user.setRole(role);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), role, user.getPhone());
        return new LoginResponse(token, true, role.name(), user.getId(), user.getName(), user.getAvatar());
    }

    private User findOrCreateByProvider(String provider, String providerId, String username, String avatar) {
        return switch (provider) {
            case "github" -> userRepository.findByGithubId(providerId)
                    .orElseGet(() -> createOAuthUser(providerId, username, avatar, "github"));
            case "gitee" -> userRepository.findByGiteeId(providerId)
                    .orElseGet(() -> createOAuthUser(providerId, username, avatar, "gitee"));
            case "wechat" -> userRepository.findByWechatOpenId(providerId)
                    .orElseGet(() -> createOAuthUser(providerId, username, avatar, "wechat"));
            case "alipay" -> userRepository.findByAlipayUserId(providerId)
                    .orElseGet(() -> createOAuthUser(providerId, username, avatar, "alipay"));
            default -> throw new BusinessException(ErrorCode.BAD_REQUEST, "不支持的登录方式: " + provider);
        };
    }

    private User createOAuthUser(String providerId, String username, String avatar, String provider) {
        User user = new User();
        user.setName(username);
        user.setAvatar(avatar);
        user.setStatus(UserStatus.ACTIVE);

        switch (provider) {
            case "github":
                user.setGithubId(providerId);
                user.setGithubUsername(username);
                break;
            case "gitee":
                user.setGiteeId(providerId);
                break;
            case "wechat":
                user.setWechatOpenId(providerId);
                break;
            case "alipay":
                user.setAlipayUserId(providerId);
                break;
        }

        return userRepository.save(user);
    }
}
