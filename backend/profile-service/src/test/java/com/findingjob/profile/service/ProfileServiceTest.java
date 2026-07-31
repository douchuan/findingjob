package com.findingjob.profile.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.profile.dto.*;
import com.findingjob.profile.entity.*;
import com.findingjob.profile.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock private JobseekerProfileRepository profileRepository;
    @Mock private SkillTagRepository skillTagRepository;
    @Mock private UserSkillRepository userSkillRepository;
    @Mock private WorkExperienceRepository workExperienceRepository;
    @Mock private CertificateRepository certificateRepository;
    @Mock private GitHubProjectRepository gitHubProjectRepository;

    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(
                profileRepository, skillTagRepository, userSkillRepository,
                workExperienceRepository, certificateRepository, gitHubProjectRepository);
    }

    // === Profile Tests ===

    @Test
    void getOrCreateProfile_existing_returnsDto() {
        JobseekerProfile profile = new JobseekerProfile();
        profile.setId(1L); profile.setUserId(100L); profile.setName("张三");
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(profile));

        var dto = profileService.getOrCreateProfile(100L, "张三");

        assertEquals("张三", dto.getName());
        verify(profileRepository, never()).save(any());
    }

    @Test
    void getOrCreateProfile_new_createsProfile() {
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.empty());
        when(profileRepository.save(any())).thenAnswer(inv -> { JobseekerProfile p = inv.getArgument(0); p.setId(1L); return p; });

        var dto = profileService.getOrCreateProfile(100L, "新用户");
        assertEquals("新用户", dto.getName());
        verify(profileRepository).save(any());
    }

    @Test
    void updateProfile_success() {
        JobseekerProfile existing = new JobseekerProfile();
        existing.setId(1L); existing.setUserId(100L);
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(existing));
        when(profileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        JobseekerProfileDto update = new JobseekerProfileDto();
        update.setBio("Dev"); update.setExpectedPosition("Backend");
        update.setYearsOfExperience(5);

        var result = profileService.updateProfile(100L, update);
        assertEquals("Dev", result.getBio());
        assertEquals("Backend", result.getExpectedPosition());
    }

    @Test
    void getPublicProfile_anonymizesName() {
        JobseekerProfile profile = new JobseekerProfile();
        profile.setId(1L); profile.setUserId(100L); profile.setName("张三");
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(profile));

        var dto = profileService.getPublicProfile(100L);
        assertEquals("张**", dto.getName());
    }

    // === Skill Tests ===

    @Test
    void addSkill_success() {
        SkillTag tag = new SkillTag();
        tag.setId(1L); tag.setName("Java"); tag.setCategory("language"); tag.setUsageCount(0);

        when(userSkillRepository.findByUserIdAndSkillId(100L, 1L)).thenReturn(Optional.empty());
        when(skillTagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(skillTagRepository.save(any())).thenAnswer(inv -> {
            SkillTag t = inv.getArgument(0); t.setUsageCount(1); return t;
        });
        when(userSkillRepository.save(any())).thenAnswer(inv -> {
            UserSkill us = inv.getArgument(0); us.setId(1L); return us;
        });

        var result = profileService.addSkill(100L, 1L, "FAMILIAR");
        assertEquals("FAMILIAR", result.getLevel());
        assertEquals("Java", result.getSkillName());
    }

    @Test
    void addSkill_duplicate_throwsException() {
        when(userSkillRepository.findByUserIdAndSkillId(100L, 1L))
                .thenReturn(Optional.of(new UserSkill()));
        assertThrows(BusinessException.class, () -> profileService.addSkill(100L, 1L, "FAMILIAR"));
    }

    @Test
    void addSkill_invalidLevel_throwsException() {
        when(userSkillRepository.findByUserIdAndSkillId(100L, 1L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> profileService.addSkill(100L, 1L, "MASTER"));
    }

    @Test
    void removeSkill_success() {
        UserSkill userSkill = new UserSkill();
        userSkill.setId(1L);
        when(userSkillRepository.findByUserIdAndSkillId(100L, 1L)).thenReturn(Optional.of(userSkill));

        profileService.removeSkill(100L, 1L);
        verify(userSkillRepository).delete(userSkill);
    }

    @Test
    void searchSkills_returnsMatches() {
        SkillTag tag = new SkillTag();
        tag.setId(1L); tag.setName("JavaScript");
        when(skillTagRepository.findByNameContainingIgnoreCase("java"))
                .thenReturn(List.of(tag));

        var results = profileService.searchSkills("java");
        assertEquals(1, results.size());
        assertEquals("JavaScript", results.get(0).getName());
    }

    // === Work Experience Tests ===

    @Test
    void addWorkExperience_success() {
        JobseekerProfile profile = new JobseekerProfile();
        profile.setId(1L); profile.setUserId(100L);
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(profile));
        when(workExperienceRepository.save(any())).thenAnswer(inv -> {
            WorkExperience e = inv.getArgument(0); e.setId(1L); return e;
        });

        WorkExperienceDto dto = new WorkExperienceDto();
        dto.setCompany("Test Corp"); dto.setPosition("Developer");
        dto.setStartDate(LocalDate.of(2020, 1, 1));

        var result = profileService.addWorkExperience(100L, dto);
        assertEquals("Test Corp", result.getCompany());
        assertEquals("Developer", result.getPosition());
    }

    @Test
    void getWorkExperiences_returnsList() {
        JobseekerProfile profile = new JobseekerProfile();
        profile.setId(1L); profile.setUserId(100L);
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(profile));
        when(workExperienceRepository.findByProfileIdOrderByStartDateDesc(1L)).thenReturn(List.of());

        var results = profileService.getWorkExperiences(100L);
        assertTrue(results.isEmpty());
    }

    // === Certificate Tests ===

    @Test
    void addCertificate_success() {
        JobseekerProfile profile = new JobseekerProfile();
        profile.setId(1L); profile.setUserId(100L);
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(profile));
        when(certificateRepository.save(any())).thenAnswer(inv -> {
            Certificate c = inv.getArgument(0); c.setId(1L); return c;
        });

        CertificateDto dto = new CertificateDto();
        dto.setName("AWS Certified"); dto.setIssuer("Amazon");
        dto.setIssueDate(LocalDate.of(2024, 6, 1));

        var result = profileService.addCertificate(100L, dto);
        assertEquals("AWS Certified", result.getName());
    }

    // === GitHub Project Tests ===

    @Test
    void syncGitHubProjects_savesTop10() {
        JobseekerProfile profile = new JobseekerProfile();
        profile.setId(1L); profile.setUserId(100L);
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(profile));
        when(gitHubProjectRepository.findByProfileIdOrderByStarCountDesc(1L)).thenReturn(List.of());
        when(gitHubProjectRepository.saveAll(any())).thenAnswer(inv -> {
            List<GitHubProject> projects = inv.getArgument(0);
            for (int i = 0; i < projects.size(); i++) projects.get(i).setId((long) i + 1);
            return projects;
        });

        List<GitHubProjectDto> projects = List.of(
                createProject("repo1", "Java", 100),
                createProject("repo2", "Python", 50),
                createProject("repo3", "Go", 200)
        );

        var result = profileService.syncGitHubProjects(100L, projects);
        assertEquals(3, result.size());
        // Should be sorted by star count desc in the repo, but we limit to 10
    }

    private GitHubProjectDto createProject(String name, String lang, int stars) {
        GitHubProjectDto dto = new GitHubProjectDto();
        dto.setRepoName(name);
        dto.setLanguage(lang);
        dto.setStarCount(stars);
        dto.setIsOwner(true);
        return dto;
    }
}
