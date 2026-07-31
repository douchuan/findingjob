package com.findingjob.rating.controller;

import com.findingjob.common.dto.ApiResponse;
import com.findingjob.common.security.JwtUserPrincipal;
import com.findingjob.rating.dto.CompanyRatingStats;
import com.findingjob.rating.dto.RatingDto;
import com.findingjob.rating.dto.ReportDto;
import com.findingjob.rating.entity.Report;
import com.findingjob.rating.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rating")
@Tag(name = "Rating", description = "Company ratings and reports")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/company/{companyId}")
    @Operation(summary = "Rate a company (jobseeker only)")
    public ApiResponse<RatingDto> rateCompany(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long companyId,
            @RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<String> tags = (List<String>) body.get("tags");
        String comment = (String) body.get("comment");
        return ApiResponse.success(ratingService.rateCompany(principal.getUserId(), companyId, tags, comment));
    }

    @GetMapping("/company/{companyId}/stats")
    @Operation(summary = "Get company rating stats (public)")
    public ApiResponse<CompanyRatingStats> getCompanyStats(@PathVariable Long companyId) {
        return ApiResponse.success(ratingService.getCompanyRatingStats(companyId));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get company ratings (public)")
    public ApiResponse<List<RatingDto>> getCompanyRatings(@PathVariable Long companyId) {
        return ApiResponse.success(ratingService.getCompanyRatings(companyId));
    }

    @PostMapping("/report")
    @Operation(summary = "Report a rating")
    public ApiResponse<ReportDto> reportRating(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody Map<String, Object> body) {
        Long ratingId = Long.parseLong(body.get("ratingId").toString());
        String reason = (String) body.get("reason");
        return ApiResponse.success(ratingService.reportRating(principal.getUserId(), ratingId, reason));
    }

    @GetMapping("/admin/reports")
    @Operation(summary = "List pending reports (admin only)")
    public ApiResponse<Page<ReportDto>> listPendingReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        return ApiResponse.success(ratingService.listPendingReports(PageRequest.of(page, pageSize)));
    }

    @PostMapping("/admin/report/{id}")
    @Operation(summary = "Handle a report (admin only)")
    public ApiResponse<ReportDto> handleReport(
            @PathVariable Long id,
            @RequestParam Report.ReportStatus action) {
        return ApiResponse.success(ratingService.handleReport(id, action));
    }
}
