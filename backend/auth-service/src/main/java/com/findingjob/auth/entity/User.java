package com.findingjob.auth.entity;

import com.findingjob.common.enums.UserRole;
import com.findingjob.common.enums.UserStatus;
import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true)
    private String phone;

    private String name;

    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    private String githubId;

    private String githubUsername;

    private String giteeId;

    private String wechatOpenId;

    private String alipayUserId;

    @Column(name = "deletion_scheduled_at")
    private LocalDateTime deletionScheduledAt;

    // Getters and setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public String getGithubId() { return githubId; }
    public void setGithubId(String githubId) { this.githubId = githubId; }
    public String getGithubUsername() { return githubUsername; }
    public void setGithubUsername(String githubUsername) { this.githubUsername = githubUsername; }
    public String getGiteeId() { return giteeId; }
    public void setGiteeId(String giteeId) { this.giteeId = giteeId; }
    public String getWechatOpenId() { return wechatOpenId; }
    public void setWechatOpenId(String wechatOpenId) { this.wechatOpenId = wechatOpenId; }
    public String getAlipayUserId() { return alipayUserId; }
    public void setAlipayUserId(String alipayUserId) { this.alipayUserId = alipayUserId; }
    public LocalDateTime getDeletionScheduledAt() { return deletionScheduledAt; }
    public void setDeletionScheduledAt(LocalDateTime deletionScheduledAt) { this.deletionScheduledAt = deletionScheduledAt; }

    public boolean isPendingDeletion() {
        return status == UserStatus.PENDING_DELETION && deletionScheduledAt != null;
    }
}
