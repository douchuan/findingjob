package com.findingjob.notification.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import com.findingjob.notification.entity.NotificationEntity;
import com.findingjob.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Create a notification.
     */
    @Transactional
    public Map<String, Object> createNotification(Long userId, String type, String content, Long relatedId) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setIsRead(false);
        notificationRepository.save(notification);

        return Map.of("id", notification.getId(), "type", type);
    }

    /**
     * Get unread notifications.
     */
    public List<Map<String, Object>> getUnread(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    /**
     * Get all notifications.
     */
    public List<Map<String, Object>> getAll(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    /**
     * Mark notification as read.
     */
    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "通知未找到"));
        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作此通知");
        }
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    /**
     * Mark all as read.
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .forEach(n -> {
                    n.setIsRead(true);
                    notificationRepository.save(n);
                });
    }

    /**
     * Get unread count.
     */
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    private Map<String, Object> toMap(NotificationEntity n) {
        return Map.of(
                "id", n.getId(),
                "type", n.getType(),
                "content", n.getContent(),
                "isRead", n.getIsRead(),
                "createdAt", n.getCreatedAt() != null ? n.getCreatedAt().toString() : null
        );
    }
}
