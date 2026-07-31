package com.findingjob.profile.dto;

import java.util.List;

public class JobseekerSearchResult {

    private Long userId;
    private String name;
    private String avatar;
    private Integer yearsOfExperience;
    private String expectedPosition;
    private List<String> skillNames;
    private Long totalVerifiedSkills;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    public String getExpectedPosition() { return expectedPosition; }
    public void setExpectedPosition(String expectedPosition) { this.expectedPosition = expectedPosition; }
    public List<String> getSkillNames() { return skillNames; }
    public void setSkillNames(List<String> skillNames) { this.skillNames = skillNames; }
    public Long getTotalVerifiedSkills() { return totalVerifiedSkills; }
    public void setTotalVerifiedSkills(Long totalVerifiedSkills) { this.totalVerifiedSkills = totalVerifiedSkills; }
}
