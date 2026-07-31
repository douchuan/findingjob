package com.findingjob.profile.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "skill_tag", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
public class SkillTag extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String category;  // e.g., "language", "framework", "tool", "database"

    @Column(name = "usage_count")
    private Integer usageCount = 0;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
}
