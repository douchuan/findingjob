package com.findingjob.profile.controller;

import com.findingjob.common.dto.ApiResponse;
import com.findingjob.common.security.JwtUserPrincipal;
import com.findingjob.profile.dto.*;
import com.findingjob.profile.entity.SkillTag;
import com.findingjob.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profile Full", description = "Profile, skills, experience, certificates, GitHub")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // === Profile ===

    @GetMapping("/me")
    @Operation(summary = "Get or create my profile")
    public ApiResponse<JobseekerProfileDto> getMyProfile(@AuthenticationPrincipal JwtUserPrincipal principal) {
        String name = principal.getPhone() != null ? principal.getPhone() : "用户";
        return ApiResponse.success(profileService.getOrCreateProfile(principal.getUserId(), name));
    }

    @PutMapping("/me")
    @Operation(summary = "Update my profile")
    public ApiResponse<JobseekerProfileDto> updateMyProfile(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody JobseekerProfileDto dto) {
        return ApiResponse.success(profileService.updateProfile(principal.getUserId(), dto));
    }

    @GetMapping("/public/{userId}")
    @Operation(summary = "Get public profile (L1, anonymized)")
    public ApiResponse<JobseekerProfileDto> getPublicProfile(@PathVariable Long userId) {
        return ApiResponse.success(profileService.getPublicProfile(userId));
    }

    // === Skills ===

    @GetMapping("/me/skills")
    @Operation(summary = "Get my skills")
    public ApiResponse<List<UserSkillDto>> getMySkills(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(profileService.getUserSkills(principal.getUserId()));
    }

    @PostMapping("/me/skills")
    @Operation(summary = "Add a skill")
    public ApiResponse<UserSkillDto> addSkill(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody Map<String, String> body) {
        Long skillId = Long.parseLong(body.get("skillId"));
        String level = body.getOrDefault("level", "FAMILIAR");
        return ApiResponse.success(profileService.addSkill(principal.getUserId(), skillId, level));
    }

    @DeleteMapping("/me/skills/{skillId}")
    @Operation(summary = "Remove a skill")
    public ApiResponse<Void> removeSkill(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long skillId) {
        profileService.removeSkill(principal.getUserId(), skillId);
        return ApiResponse.success();
    }

    @GetMapping("/skills/search")
    @Operation(summary = "Search skill tags")
    public ApiResponse<List<SkillTag>> searchSkills(@RequestParam String q) {
        return ApiResponse.success(profileService.searchSkills(q));
    }

    @GetMapping("/skills")
    @Operation(summary = "Get all skill tags")
    public ApiResponse<List<SkillTag>> getAllSkills() {
        return ApiResponse.success(profileService.getAllSkillTags());
    }

    // === Work Experience ===

    @GetMapping("/me/experiences")
    @Operation(summary = "Get my work experiences")
    public ApiResponse<List<WorkExperienceDto>> getMyExperiences(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(profileService.getWorkExperiences(principal.getUserId()));
    }

    @PostMapping("/me/experiences")
    @Operation(summary = "Add work experience")
    public ApiResponse<WorkExperienceDto> addExperience(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody WorkExperienceDto dto) {
        return ApiResponse.success(profileService.addWorkExperience(principal.getUserId(), dto));
    }

    @PutMapping("/me/experiences/{expId}")
    @Operation(summary = "Update work experience")
    public ApiResponse<WorkExperienceDto> updateExperience(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long expId,
            @RequestBody WorkExperienceDto dto) {
        return ApiResponse.success(profileService.updateWorkExperience(principal.getUserId(), expId, dto));
    }

    @DeleteMapping("/me/experiences/{expId}")
    @Operation(summary = "Delete work experience")
    public ApiResponse<Void> deleteExperience(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long expId) {
        profileService.deleteWorkExperience(principal.getUserId(), expId);
        return ApiResponse.success();
    }

    // === Certificates ===

    @GetMapping("/me/certificates")
    @Operation(summary = "Get my certificates")
    public ApiResponse<List<CertificateDto>> getMyCertificates(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(profileService.getCertificates(principal.getUserId()));
    }

    @PostMapping("/me/certificates")
    @Operation(summary = "Add certificate")
    public ApiResponse<CertificateDto> addCertificate(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody CertificateDto dto) {
        return ApiResponse.success(profileService.addCertificate(principal.getUserId(), dto));
    }

    @DeleteMapping("/me/certificates/{certId}")
    @Operation(summary = "Delete certificate")
    public ApiResponse<Void> deleteCertificate(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long certId) {
        profileService.deleteCertificate(principal.getUserId(), certId);
        return ApiResponse.success();
    }

    // === GitHub Projects ===

    @GetMapping("/me/github")
    @Operation(summary = "Get my GitHub projects")
    public ApiResponse<List<GitHubProjectDto>> getMyGitHubProjects(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.success(profileService.getGitHubProjects(principal.getUserId()));
    }

    @PostMapping("/me/github/sync")
    @Operation(summary = "Sync GitHub projects (from external OAuth)")
    public ApiResponse<List<GitHubProjectDto>> syncGitHubProjects(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody List<GitHubProjectDto> projects) {
        return ApiResponse.success(profileService.syncGitHubProjects(principal.getUserId(), projects));
    }
}
