package com.findingjob.notification.controller;

import com.findingjob.common.dto.ApiResponse;
import com.findingjob.common.security.JwtUserPrincipal;
import com.findingjob.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
@Tag(name = "Notification", description = "In-app notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications")
    public ApiResponse<List<Map<String, Object>>> getUnread(
            @AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(notificationService.getUnread(principal.getUserId()));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all notifications")
    public ApiResponse<List<Map<String, Object>>> getAll(
            @AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(notificationService.getAll(principal.getUserId()));
    }

    @PostMapping("/read/{id}")
    @Operation(summary = "Mark notification as read")
    public ApiResponse<Void> markAsRead(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id) {
        notificationService.markAsRead(principal.getUserId(), id);
        return ApiResponse.success();
    }

    @PostMapping("/read-all")
    @Operation(summary = "Mark all as read")
    public ApiResponse<Void> markAllAsRead(@AuthenticationPrincipal JwtUserPrincipal principal) {
        notificationService.markAllAsRead(principal.getUserId());
        return ApiResponse.success();
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get unread count")
    public ApiResponse<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(Map.of("count", notificationService.getUnreadCount(principal.getUserId())));
    }
}
