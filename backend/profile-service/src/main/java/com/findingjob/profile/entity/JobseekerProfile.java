package com.findingjob.profile.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "jobseeker_profile")
public class JobseekerProfile extends BaseEntity {

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    private String name;

    private String bio;

    @Column(name = "expected_position")
    private String expectedPosition;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    private String avatar;

    @Column(name = "github_token")
    private String githubToken;

    @Column(name = "gitee_token")
    private String giteeToken;

    // Getters and setters
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
