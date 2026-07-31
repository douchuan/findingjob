package com.findingjob.profile.dto;

public class JobseekerProfileDto {

    private Long id;
    private Long userId;
    private String name;
    private String bio;
    private String expectedPosition;
    private Integer yearsOfExperience;
    private String avatar;
    private String githubToken;
    private String giteeToken;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getExpectedPosition() { return expectedPosition; }
    public void setExpectedPosition(String expectedPosition) { this.expectedPosition = expectedPosition; }
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getGithubToken() { return githubToken; }
    public void setGithubToken(String githubToken) { this.githubToken = githubToken; }
    public String getGiteeToken() { return giteeToken; }
    public void setGiteeToken(String giteeToken) { this.giteeToken = giteeToken; }
}
