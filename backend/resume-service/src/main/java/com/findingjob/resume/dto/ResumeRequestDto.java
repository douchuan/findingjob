package com.findingjob.resume.dto;

import java.time.LocalDateTime;

public class ResumeRequestDto {

    private Long id;
    private Long hrId;
    private Long jobseekerId;
    private String status;
    private String downloadUrl;
    private LocalDateTime expiresAt;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHrId() { return hrId; }
    public void setHrId(Long hrId) { this.hrId = hrId; }
    public Long getJobseekerId() { return jobseekerId; }
    public void setJobseekerId(Long jobseekerId) { this.jobseekerId = jobseekerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
