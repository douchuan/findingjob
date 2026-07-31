package com.findingjob.company.dto;

public class CompanyDto {

    private Long id;
    private String name;
    private String industry;
    private String size;
    private String description;
    private String logoUrl;
    private String verificationStatus;
    private String licenseFileKey;
    private String reviewComment;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
    public String getLicenseFileKey() { return licenseFileKey; }
    public void setLicenseFileKey(String licenseFileKey) { this.licenseFileKey = licenseFileKey; }
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
}
