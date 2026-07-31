package com.findingjob.profile.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "certificate")
public class Certificate extends BaseEntity {

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    private String name;

    private String issuer;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "image_url")
    private String imageUrl;

    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
