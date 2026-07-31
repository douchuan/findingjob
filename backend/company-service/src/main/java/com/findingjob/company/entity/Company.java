package com.findingjob.company.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "company")
public class Company extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String industry;

    private String size;  // e.g., "1-50", "50-200", "200-500", "500+"

    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "license_file_key")
    private String licenseFileKey;

    @Column(name = "review_comment")
    private String reviewComment;

    public enum VerificationStatus {
        PENDING, APPROVED, REJECTED
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
    public String getLicenseFileKey() { return licenseFileKey; }
    public void setLicenseFileKey(String licenseFileKey) { this.licenseFileKey = licenseFileKey; }
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
}
