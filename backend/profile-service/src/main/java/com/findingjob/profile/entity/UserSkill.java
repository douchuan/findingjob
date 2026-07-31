package com.findingjob.profile.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "user_skill", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "skill_id"})
})
public class UserSkill extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "skill_id", nullable = false)
    private Long skillId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel level = SkillLevel.FAMILIAR;

    @Column(name = "verified_count")
    private Integer verifiedCount = 0;

    public enum SkillLevel {
        BEGINNER("了解"),
        FAMILIAR("熟练"),
        EXPERT("精通");

        private final String label;
        SkillLevel(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }
    public SkillLevel getLevel() { return level; }
    public void setLevel(SkillLevel level) { this.level = level; }
    public Integer getVerifiedCount() { return verifiedCount; }
    public void setVerifiedCount(Integer verifiedCount) { this.verifiedCount = verifiedCount; }
}
