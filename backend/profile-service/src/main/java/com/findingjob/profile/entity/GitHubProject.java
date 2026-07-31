package com.findingjob.profile.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "github_project")
public class GitHubProject extends BaseEntity {

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    private String repoName;

    private String description;

    private String language;

    @Column(name = "star_count")
    private Integer starCount;

    private String url;

    @Column(name = "is_owner")
    private Boolean isOwner;

    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }
    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Integer getStarCount() { return starCount; }
    public void setStarCount(Integer starCount) { this.starCount = starCount; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Boolean getIsOwner() { return isOwner; }
    public void setIsOwner(Boolean isOwner) { this.isOwner = isOwner; }
}
