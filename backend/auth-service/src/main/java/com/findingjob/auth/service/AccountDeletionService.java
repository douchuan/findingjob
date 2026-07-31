package com.findingjob.auth.service;

import com.findingjob.auth.entity.User;
import com.findingjob.auth.repository.UserRepository;
import com.findingjob.common.enums.UserStatus;
import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AccountDeletionService {

    private final UserRepository userRepository;

    public AccountDeletionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Request account deletion — sets 7-day cooling period.
     */
    @Transactional
    public Map<String, Object> requestDeletion(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getStatus() == UserStatus.PENDING_DELETION) {
            throw new BusinessException(ErrorCode.ACCOUNT_PENDING_DELETION);
        }

        user.setStatus(UserStatus.PENDING_DELETION);
        user.setDeletionScheduledAt(LocalDateTime.now().plusDays(7));
        userRepository.save(user);

        return Map.of(
                "status", "pending_deletion",
                "scheduledAt", user.getDeletionScheduledAt().toString(),
                "message", "账号将在7天后注销，期间可撤销"
        );
    }

    /**
     * Cancel pending deletion.
     */
    @Transactional
    public Map<String, Object> cancelDeletion(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getStatus() != UserStatus.PENDING_DELETION) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "账号不在注销流程中");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setDeletionScheduledAt(null);
        userRepository.save(user);

        return Map.of("status", "active", "message", "注销已撤销");
    }

    /**
     * Process expired deletion requests — runs daily.
     * Anonymizes ratings, deletes personal data.
     */
    @Scheduled(cron = "0 0 2 * * *")  // Every day at 2am
    @Transactional
    public void processExpiredDeletions() {
        LocalDateTime now = LocalDateTime.now();

        userRepository.findAll().stream()
                .filter(u -> u.getStatus() == UserStatus.PENDING_DELETION
                        && u.getDeletionScheduledAt() != null
                        && u.getDeletionScheduledAt().isBefore(now))
                .forEach(this::deleteUserData);
    }

    /**
     * Delete user's personal data.
     * Note: ratings are anonymized (not deleted) to maintain system integrity.
     */
    private void deleteUserData(User user) {
        // Clear personal data
        user.setName("已注销用户");
        user.setPhone(null);
        user.setAvatar(null);
        user.setGithubId(null);
        user.setGithubUsername(null);
        user.setGiteeId(null);
        user.setWechatOpenId(null);
        user.setAlipayUserId(null);
        user.setStatus(UserStatus.DELETED);
        user.setDeletionScheduledAt(null);

        userRepository.save(user);
    }
}
