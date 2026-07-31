package com.findingjob.profile.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import com.findingjob.profile.dto.*;
import com.findingjob.profile.entity.*;
import com.findingjob.profile.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final JobseekerProfileRepository profileRepository;
    private final SkillTagRepository skillTagRepository;
    private final UserSkillRepository userSkillRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final CertificateRepository certificateRepository;
    private final GitHubProjectRepository gitHubProjectRepository;

    public ProfileService(JobseekerProfileRepository profileRepository,
                          SkillTagRepository skillTagRepository,
                          UserSkillRepository userSkillRepository,
                          WorkExperienceRepository workExperienceRepository,
                          CertificateRepository certificateRepository,
                          GitHubProjectRepository gitHubProjectRepository) {
        this.profileRepository = profileRepository;
        this.skillTagRepository = skillTagRepository;
        this.userSkillRepository = userSkillRepository;
        this.workExperienceRepository = workExperienceRepository;
        this.certificateRepository = certificateRepository;
        this.gitHubProjectRepository = gitHubProjectRepository;
    }

    // === JobseekerProfile ===

    @Transactional
    public JobseekerProfileDto getOrCreateProfile(Long userId, String name) {
        return profileRepository.findByUserId(userId)
                .map(this::toDto)
                .orElseGet(() -> {
                    JobseekerProfile profile = new JobseekerProfile();
                    profile.setUserId(userId);
                    profile.setName(name != null ? name : "");
                    JobseekerProfile saved = profileRepository.save(profile);
                    return toDto(saved);
                });
    }

    @Transactional
    public JobseekerProfileDto updateProfile(Long userId, JobseekerProfileDto dto) {
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));

        profile.setBio(dto.getBio());
        profile.setExpectedPosition(dto.getExpectedPosition());
        profile.setYearsOfExperience(dto.getYearsOfExperience());
        profile.setAvatar(dto.getAvatar());
        profile.setGithubToken(dto.getGithubToken());
        profile.setGiteeToken(dto.getGiteeToken());

        return toDto(profileRepository.save(profile));
    }

    public JobseekerProfileDto getPublicProfile(Long userId) {
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));

        JobseekerProfileDto dto = toDto(profile);
        String name = dto.getName();
        if (name != null && name.length() > 1) {
            dto.setName(name.charAt(0) + "**");
        }
        return dto;
    }

    // === Skill Tags ===

    @Transactional
    public UserSkillDto addSkill(Long userId, Long skillId, String level) {
        if (userSkillRepository.findByUserIdAndSkillId(userId, skillId).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_ENTRY, "技能已添加");
        }

        UserSkill userSkill = new UserSkill();
        userSkill.setUserId(userId);
        userSkill.setSkillId(skillId);
        try {
            userSkill.setLevel(UserSkill.SkillLevel.valueOf(level.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "无效的技能等级，支持：BEGINNER/FAMILIAR/EXPERT");
        }
        userSkill.setVerifiedCount(0);

        // Increment usage count
        skillTagRepository.findById(skillId).ifPresent(tag -> {
            tag.setUsageCount(tag.getUsageCount() + 1);
            skillTagRepository.save(tag);
        });

        return toUserSkillDto(userSkillRepository.save(userSkill));
    }

    @Transactional
    public void removeSkill(Long userId, Long skillId) {
        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skillId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "技能未找到"));
        userSkillRepository.delete(userSkill);
    }

    public List<UserSkillDto> getUserSkills(Long userId) {
        return userSkillRepository.findByUserIdOrderByVerified(userId).stream()
                .map(this::toUserSkillDto)
                .collect(Collectors.toList());
    }

    public List<SkillTag> searchSkills(String query) {
        return skillTagRepository.findByNameContainingIgnoreCase(query);
    }

    public List<SkillTag> getAllSkillTags() {
        return skillTagRepository.findAll();
    }

    // === Work Experience ===

    @Transactional
    public WorkExperienceDto addWorkExperience(Long userId, WorkExperienceDto dto) {
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));

        WorkExperience exp = new WorkExperience();
        exp.setProfileId(profile.getId());
        exp.setCompany(dto.getCompany());
        exp.setPosition(dto.getPosition());
        exp.setStartDate(dto.getStartDate());
        exp.setEndDate(dto.getEndDate());
        exp.setDescription(dto.getDescription());

        return toWorkExpDto(workExperienceRepository.save(exp));
    }

    @Transactional
    public WorkExperienceDto updateWorkExperience(Long userId, Long expId, WorkExperienceDto dto) {
        WorkExperience exp = workExperienceRepository.findById(expId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "工作经历未找到"));

        // Verify ownership
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));
        if (!exp.getProfileId().equals(profile.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权编辑此工作经历");
        }

        exp.setCompany(dto.getCompany());
        exp.setPosition(dto.getPosition());
        exp.setStartDate(dto.getStartDate());
        exp.setEndDate(dto.getEndDate());
        exp.setDescription(dto.getDescription());

        return toWorkExpDto(workExperienceRepository.save(exp));
    }

    @Transactional
    public void deleteWorkExperience(Long userId, Long expId) {
        WorkExperience exp = workExperienceRepository.findById(expId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "工作经历未找到"));

        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));
        if (!exp.getProfileId().equals(profile.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权删除此工作经历");
        }

        workExperienceRepository.delete(exp);
    }

    public List<WorkExperienceDto> getWorkExperiences(Long userId) {
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));

        return workExperienceRepository.findByProfileIdOrderByStartDateDesc(profile.getId()).stream()
                .map(this::toWorkExpDto)
                .collect(Collectors.toList());
    }

    // === Certificates ===

    @Transactional
    public CertificateDto addCertificate(Long userId, CertificateDto dto) {
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));

        Certificate cert = new Certificate();
        cert.setProfileId(profile.getId());
        cert.setName(dto.getName());
        cert.setIssuer(dto.getIssuer());
        cert.setIssueDate(dto.getIssueDate());
        cert.setImageUrl(dto.getImageUrl());

        return toCertDto(certificateRepository.save(cert));
    }

    @Transactional
    public void deleteCertificate(Long userId, Long certId) {
        Certificate cert = certificateRepository.findById(certId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "证书未找到"));

        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));
        if (!cert.getProfileId().equals(profile.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权删除此证书");
        }

        certificateRepository.delete(cert);
    }

    public List<CertificateDto> getCertificates(Long userId) {
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));

        return certificateRepository.findByProfileIdOrderByIssueDateDesc(profile.getId()).stream()
                .map(this::toCertDto)
                .collect(Collectors.toList());
    }

    // === GitHub Projects ===

    public List<GitHubProjectDto> getGitHubProjects(Long userId) {
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));

        return gitHubProjectRepository.findByProfileIdOrderByStarCountDesc(profile.getId()).stream()
                .map(this::toGitHubDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<GitHubProjectDto> syncGitHubProjects(Long userId, List<GitHubProjectDto> projects) {
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));

        // Delete existing projects
        List<GitHubProject> existing = gitHubProjectRepository.findByProfileIdOrderByStarCountDesc(profile.getId());
        gitHubProjectRepository.deleteAll(existing);

        // Save new projects (Top 10)
        List<GitHubProject> saved = projects.stream()
                .limit(10)
                .map(dto -> {
                    GitHubProject p = new GitHubProject();
                    p.setProfileId(profile.getId());
                    p.setRepoName(dto.getRepoName());
                    p.setDescription(dto.getDescription());
                    p.setLanguage(dto.getLanguage());
                    p.setStarCount(dto.getStarCount());
                    p.setUrl(dto.getUrl());
                    p.setIsOwner(dto.getIsOwner());
                    return p;
                })
                .collect(Collectors.toList());

        return gitHubProjectRepository.saveAll(saved).stream()
                .map(this::toGitHubDto)
                .collect(Collectors.toList());
    }

    // === Mappers ===

    private JobseekerProfileDto toDto(JobseekerProfile profile) {
        JobseekerProfileDto dto = new JobseekerProfileDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUserId());
        dto.setName(profile.getName());
        dto.setBio(profile.getBio());
        dto.setExpectedPosition(profile.getExpectedPosition());
        dto.setYearsOfExperience(profile.getYearsOfExperience());
        dto.setAvatar(profile.getAvatar());
        dto.setGithubToken(profile.getGithubToken());
        dto.setGiteeToken(profile.getGiteeToken());
        return dto;
    }

    private UserSkillDto toUserSkillDto(UserSkill userSkill) {
        UserSkillDto dto = new UserSkillDto();
        dto.setId(userSkill.getId());
        dto.setSkillId(userSkill.getSkillId());
        dto.setLevel(userSkill.getLevel().name());
        dto.setVerifiedCount(userSkill.getVerifiedCount());

        skillTagRepository.findById(userSkill.getSkillId()).ifPresent(tag -> {
            dto.setSkillName(tag.getName());
            dto.setCategory(tag.getCategory());
        });

        return dto;
    }

    private WorkExperienceDto toWorkExpDto(WorkExperience exp) {
        WorkExperienceDto dto = new WorkExperienceDto();
        dto.setId(exp.getId());
        dto.setCompany(exp.getCompany());
        dto.setPosition(exp.getPosition());
        dto.setStartDate(exp.getStartDate());
        dto.setEndDate(exp.getEndDate());
        dto.setDescription(exp.getDescription());
        return dto;
    }

    private CertificateDto toCertDto(Certificate cert) {
        CertificateDto dto = new CertificateDto();
        dto.setId(cert.getId());
        dto.setName(cert.getName());
        dto.setIssuer(cert.getIssuer());
        dto.setIssueDate(cert.getIssueDate());
        dto.setImageUrl(cert.getImageUrl());
        return dto;
    }

    private GitHubProjectDto toGitHubDto(GitHubProject project) {
        GitHubProjectDto dto = new GitHubProjectDto();
        dto.setId(project.getId());
        dto.setRepoName(project.getRepoName());
        dto.setDescription(project.getDescription());
        dto.setLanguage(project.getLanguage());
        dto.setStarCount(project.getStarCount());
        dto.setUrl(project.getUrl());
        dto.setIsOwner(project.getIsOwner());
        return dto;
    }

    // === Search (Ticket 05) ===

    /**
     * Search jobseekers by skill keyword, experience range, and expected position.
     * Returns anonymized public info with skill matches.
     */
    public List<JobseekerSearchResult> searchJobseekers(
            String skillKeyword, Integer minExperience, Integer maxExperience, String positionKeyword) {

        // Find matching skill IDs
        List<Long> matchingSkillIds;
        if (skillKeyword != null && !skillKeyword.isEmpty()) {
            matchingSkillIds = skillTagRepository.findByNameContainingIgnoreCase(skillKeyword)
                    .stream().map(SkillTag::getId).toList();
        } else {
            matchingSkillIds = List.of();
        }

        // Find all profiles with matching skills
        List<UserSkill> matchingSkills;
        if (!matchingSkillIds.isEmpty()) {
            matchingSkills = userSkillRepository.findAll().stream()
                    .filter(us -> matchingSkillIds.contains(us.getSkillId()))
                    .toList();
        } else {
            matchingSkills = userSkillRepository.findAll();
        }

        // Group by user and build results
        Map<Long, List<UserSkill>> byUser = matchingSkills.stream()
                .collect(Collectors.groupingBy(UserSkill::getUserId));

        return byUser.entrySet().stream()
                .map(entry -> {
                    Long userId = entry.getKey();
                    List<UserSkill> skills = entry.getValue();

                    JobseekerProfile profile = profileRepository.findByUserId(userId).orElse(null);
                    if (profile == null) return null;

                    // Filter by experience
                    if (minExperience != null && (profile.getYearsOfExperience() == null || profile.getYearsOfExperience() < minExperience))
                        return null;
                    if (maxExperience != null && (profile.getYearsOfExperience() == null || profile.getYearsOfExperience() > maxExperience))
                        return null;

                    // Filter by position
                    if (positionKeyword != null && !positionKeyword.isEmpty()) {
                        String pos = profile.getExpectedPosition();
                        if (pos == null || !pos.toLowerCase().contains(positionKeyword.toLowerCase()))
                            return null;
                    }

                    // Build result
                    List<SkillTag> skillTags = skills.stream()
                            .map(us -> skillTagRepository.findById(us.getSkillId()).orElse(null))
                            .filter(Objects::nonNull)
                            .toList();

                    long totalVerified = skills.stream()
                            .mapToLong(UserSkill::getVerifiedCount)
                            .sum();

                    JobseekerSearchResult result = new JobseekerSearchResult();
                    result.setUserId(userId);
                    // Anonymize name
                    String name = profile.getName();
                    result.setName(name != null && name.length() > 1 ? name.charAt(0) + "**" : name);
                    result.setAvatar(profile.getAvatar());
                    result.setYearsOfExperience(profile.getYearsOfExperience());
                    result.setExpectedPosition(profile.getExpectedPosition());
                    result.setSkillNames(skillTags.stream().map(SkillTag::getName).toList());
                    result.setTotalVerifiedSkills(totalVerified);
                    return result;
                })
                .filter(Objects::nonNull)
                .sorted((a, b) -> Long.compare(
                        b.getTotalVerifiedSkills() != null ? b.getTotalVerifiedSkills() : 0,
                        a.getTotalVerifiedSkills() != null ? a.getTotalVerifiedSkills() : 0
                ))
                .toList();
    }
}
