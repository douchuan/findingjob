package com.findingjob.rating.dto;

import java.util.List;

public class RatingDto {

    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private Long toCompanyId;
    private List<String> tags;
    private String comment;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
