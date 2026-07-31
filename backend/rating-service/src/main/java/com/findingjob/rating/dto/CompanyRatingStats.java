package com.findingjob.rating.dto;

import java.util.List;

public class CompanyRatingStats {

    private Long companyId;
    private long positiveCount;
    private long negativeCount;
    private List<String> positiveTags;
    private List<String> negativeTags;

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public long getPositiveCount() { return positiveCount; }
    public void setPositiveCount(long positiveCount) { this.positiveCount = positiveCount; }
    public long getNegativeCount() { return negativeCount; }
    public void setNegativeCount(long negativeCount) { this.negativeCount = negativeCount; }
    public List<String> getPositiveTags() { return positiveTags; }
    public void setPositiveTags(List<String> positiveTags) { this.positiveTags = positiveTags; }
    public List<String> getNegativeTags() { return negativeTags; }
    public void setNegativeTags(List<String> negativeTags) { this.negativeTags = negativeTags; }
}
