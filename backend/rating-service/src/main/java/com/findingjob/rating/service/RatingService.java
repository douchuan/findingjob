package com.findingjob.rating.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import com.findingjob.rating.dto.CompanyRatingStats;
import com.findingjob.rating.dto.RatingDto;
import com.findingjob.rating.dto.ReportDto;
import com.findingjob.rating.entity.Rating;
import com.findingjob.rating.entity.Report;
import com.findingjob.rating.entity.Report.ReportStatus;
import com.findingjob.rating.repository.RatingRepository;
import com.findingjob.rating.repository.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ReportRepository reportRepository;

    // Conduct tag definitions
    private static final Set<String> POSITIVE_COMPANY_TAGS = Set.of("招聘规范", "承诺兑现", "沟通及时", "流程透明");
    private static final Set<String> NEGATIVE_COMPANY_TAGS = Set.of("招聘不规范", "承诺未兑现", "面试不当", "薪资不符");
    private static final Set<String> POSITIVE_USER_TAGS = Set.of("守约", "信息真实", "沟通顺畅");
    private static final Set<String> NEGATIVE_USER_TAGS = Set.of("面试爽约", "信息不实", "沟通不畅");

    public RatingService(RatingRepository ratingRepository, ReportRepository reportRepository) {
        this.ratingRepository = ratingRepository;
        this.reportRepository = reportRepository;
    }

    /**
     * Jobseeker rates a company.
     */
    @Transactional
    public RatingDto rateCompany(Long userId, Long companyId, List<String> tags, String comment) {
        // Validate tags
        for (String tag : tags) {
            if (!POSITIVE_COMPANY_TAGS.contains(tag) && !NEGATIVE_COMPANY_TAGS.contains(tag)) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "无效的评价标签: " + tag);
            }
        }

        // Check if user already rated this company (update last rating)
        List<Rating> existing = ratingRepository.findByFromUserIdAndToCompanyId(userId, companyId);

        Rating rating;
        if (!existing.isEmpty()) {
            rating = existing.get(existing.size() - 1);
            rating.setTags(tags);
            rating.setComment(comment);
        } else {
            rating = new Rating();
            rating.setFromUserId(userId);
            rating.setToCompanyId(companyId);
            rating.setTags(tags);
            rating.setComment(comment);
            rating.setIsHidden(false);
        }

        return toDto(ratingRepository.save(rating));
    }

    /**
     * Get company rating stats.
     */
    public CompanyRatingStats getCompanyRatingStats(Long companyId) {
        List<Rating> ratings = ratingRepository.findByToCompanyIdAndIsHiddenFalse(companyId);

        Set<String> positiveTags = new HashSet<>();
        Set<String> negativeTags = new HashSet<>();
        long positiveCount = 0;
        long negativeCount = 0;

        for (Rating r : ratings) {
            boolean hasNegative = r.getTags().stream().anyMatch(NEGATIVE_COMPANY_TAGS::contains);
            boolean hasPositive = r.getTags().stream().anyMatch(POSITIVE_COMPANY_TAGS::contains);

            if (hasPositive) positiveCount++;
            if (hasNegative) negativeCount++;

            positiveTags.addAll(r.getTags().stream().filter(POSITIVE_COMPANY_TAGS::contains).toList());
            negativeTags.addAll(r.getTags().stream().filter(NEGATIVE_COMPANY_TAGS::contains).toList());
        }

        CompanyRatingStats stats = new CompanyRatingStats();
        stats.setCompanyId(companyId);
        stats.setPositiveCount(positiveCount);
        stats.setNegativeCount(negativeCount);
        stats.setPositiveTags(new ArrayList<>(positiveTags));
        stats.setNegativeTags(new ArrayList<>(negativeTags));
        return stats;
    }

    /**
     * Get ratings for a company (public).
     */
    public List<RatingDto> getCompanyRatings(Long companyId) {
        return ratingRepository.findByToCompanyIdAndIsHiddenFalse(companyId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Report a rating.
     */
    @Transactional
    public ReportDto reportRating(Long reporterId, Long ratingId, String reason) {
        Report report = new Report();
        report.setReporterId(reporterId);
        report.setTargetType("rating");
        report.setTargetId(ratingId);
        report.setReason(reason);
        report.setStatus(ReportStatus.PENDING);
        return toReportDto(reportRepository.save(report));
    }

    /**
     * Admin: handle a report.
     */
    @Transactional
    public ReportDto handleReport(Long reportId, ReportStatus action) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "举报未找到"));

        report.setStatus(action);

        if (action == ReportStatus.ACCEPTED) {
            ratingRepository.findById(report.getTargetId())
                    .ifPresent(rating -> {
                        rating.setIsHidden(true);
                        ratingRepository.save(rating);
                    });
        }

        return toReportDto(report);
    }

    /**
     * Admin: list pending reports.
     */
    public Page<ReportDto> listPendingReports(Pageable pageable) {
        return reportRepository.findByStatus(ReportStatus.PENDING, pageable)
                .map(this::toReportDto);
    }

    private RatingDto toDto(Rating rating) {
        RatingDto dto = new RatingDto();
        dto.setId(rating.getId());
        dto.setFromUserId(rating.getFromUserId());
        dto.setToUserId(rating.getToUserId());
        dto.setToCompanyId(rating.getToCompanyId());
        dto.setTags(rating.getTags());
        dto.setComment(rating.getComment());
        dto.setCreatedAt(rating.getCreatedAt() != null ? rating.getCreatedAt().toString() : null);
        return dto;
    }

    private ReportDto toReportDto(Report report) {
        ReportDto dto = new ReportDto();
        dto.setId(report.getId());
        dto.setReporterId(report.getReporterId());
        dto.setTargetType(report.getTargetType());
        dto.setTargetId(report.getTargetId());
        dto.setReason(report.getReason());
        dto.setStatus(report.getStatus().name());
        return dto;
    }
}
