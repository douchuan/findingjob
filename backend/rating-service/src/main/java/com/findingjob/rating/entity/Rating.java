package com.findingjob.rating.entity;

import com.findingjob.common.model.BaseEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rating")
public class Rating extends BaseEntity {

    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId;

    @Column(name = "to_user_id")
    private Long toUserId;

    @Column(name = "to_company_id")
    private Long toCompanyId;

    /** Positive or negative conduct tags */
    @ElementCollection
    @CollectionTable(name = "rating_tags", joinColumns = @JoinColumn(name = "rating_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "is_hidden")
    private Boolean isHidden = false;

    // Getters and setters
    public Long getFromUserId() { return fromUserId; }
    public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }
    public Long getToUserId() { return toUserId; }
    public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
    public Long getToCompanyId() { return toCompanyId; }
    public void setToCompanyId(Long toCompanyId) { this.toCompanyId = toCompanyId; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Boolean getIsHidden() { return isHidden; }
    public void setIsHidden(Boolean isHidden) { this.isHidden = isHidden; }
}
