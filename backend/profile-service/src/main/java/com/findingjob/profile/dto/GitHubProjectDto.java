package com.findingjob.profile.dto;

public class GitHubProjectDto {

    private Long id;
    private String repoName;
    private String description;
    private String language;
    private Integer starCount;
    private String url;
    private Boolean isOwner;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
