package com.findingjob.resume.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resume_request")
public class ResumeRequest extends BaseEntity {

    @Column(name = "hr_id", nullable = false)
    private Long hrId;

    @Column(name = "jobseeker_id", nullable = false)
    private Long jobseekerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED, EXPIRED
    }

    public Long getHrId() { return hrId; }
    public void setHrId(Long hrId) { this.hrId = hrId; }
    public Long getJobseekerId() { return jobseekerId; }
    public void setJobseekerId(Long jobseekerId) { this.jobseekerId = jobseekerId; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
