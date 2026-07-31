package com.findingjob.profile.dto;

public class UserSkillDto {

    private Long id;
    private Long skillId;
    private String skillName;
    private String category;
    private String level;
    private Integer verifiedCount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public Integer getVerifiedCount() { return verifiedCount; }
    public void setVerifiedCount(Integer verifiedCount) { this.verifiedCount = verifiedCount; }
}
