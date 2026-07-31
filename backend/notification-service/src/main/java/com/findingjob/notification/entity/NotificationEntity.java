package com.findingjob.notification.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "notification")
public class NotificationEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String type;  // "resume_request", "resume_response", "rating", "report", "interest"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "related_id")
    private Long relatedId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public Long getRelatedId() { return relatedId; }
    public void setRelatedId(Long relatedId) { this.relatedId = relatedId; }
}
