package com.findingjob.resume.controller;

import com.findingjob.common.dto.ApiResponse;
import com.findingjob.common.security.JwtUserPrincipal;
import com.findingjob.resume.dto.ResumeRequestDto;
import com.findingjob.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@Tag(name = "Resume", description = "Resume upload and request flow")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Upload resume (PDF only)")
    public ApiResponse<Void> uploadResume(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestParam("file") MultipartFile file) {
        resumeService.uploadResume(principal.getUserId(), file);
        return ApiResponse.success();
    }

    @PostMapping("/request/{jobseekerId}")
    @Operation(summary = "Request to view resume")
    public ApiResponse<ResumeRequestDto> requestResume(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long jobseekerId) {
        return ApiResponse.success(resumeService.requestResume(principal.getUserId(), jobseekerId));
    }

    @PostMapping("/respond/{requestId}")
    @Operation(summary = "Approve/reject resume request")
    public ApiResponse<ResumeRequestDto> respondToRequest(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long requestId,
            @RequestBody Map<String, Boolean> body) {
        boolean approve = Boolean.TRUE.equals(body.get("approve"));
        return ApiResponse.success(resumeService.respondToRequest(requestId, principal.getUserId(), approve));
    }

    @GetMapping("/requests/pending")
    @Operation(summary = "Get pending requests (jobseeker)")
    public ApiResponse<List<ResumeRequestDto>> getPendingRequests(
            @AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(resumeService.getPendingRequests(principal.getUserId()));
    }

    @GetMapping("/requests/my")
    @Operation(summary = "Get my requests (HR)")
    public ApiResponse<List<ResumeRequestDto>> getMyRequests(
            @AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(resumeService.getMyRequests(principal.getUserId()));
    }

    @GetMapping("/check/{jobseekerId}")
    @Operation(summary = "Check if HR has approved access")
    public ApiResponse<Map<String, Boolean>> checkAccess(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long jobseekerId) {
        boolean hasAccess = resumeService.hasApprovedRequest(principal.getUserId(), jobseekerId);
        return ApiResponse.success(Map.of("hasAccess", hasAccess));
    }
}
