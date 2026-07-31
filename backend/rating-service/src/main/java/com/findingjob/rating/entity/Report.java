package com.findingjob.rating.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "report")
public class Report extends BaseEntity {

    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    @Column(name = "target_type", nullable = false)
    private String targetType;  // "rating"

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    private String reason;  // "malicious", "insult", "false"

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    public enum ReportStatus {
        PENDING, ACCEPTED, REJECTED
    }

    public Long getReporterId() { return reporterId; }
    public void setReporterId(Long reporterId) { this.reporterId = reporterId; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
}
