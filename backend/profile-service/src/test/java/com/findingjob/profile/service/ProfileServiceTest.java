package com.findingjob.profile.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.profile.dto.JobseekerProfileDto;
import com.findingjob.profile.entity.JobseekerProfile;
import com.findingjob.profile.repository.JobseekerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private JobseekerProfileRepository profileRepository;

    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(profileRepository);
    }

    @Test
    void getOrCreateProfile_existing_returnsDto() {
        JobseekerProfile profile = new JobseekerProfile();
        profile.setId(1L);
        profile.setUserId(100L);
        profile.setName("张三");
        profile.setBio("Java developer");
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(profile));

        JobseekerProfileDto dto = profileService.getOrCreateProfile(100L, "张三");

        assertEquals(1L, dto.getId());
        assertEquals("张三", dto.getName());
        assertEquals("Java developer", dto.getBio());
        verify(profileRepository, never()).save(any());
    }

    @Test
    void getOrCreateProfile_new_createsProfile() {
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.empty());
        when(profileRepository.save(any(JobseekerProfile.class))).thenAnswer(invocation -> {
            JobseekerProfile p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        JobseekerProfileDto dto = profileService.getOrCreateProfile(100L, "新用户");

        assertEquals(1L, dto.getId());
        assertEquals("新用户", dto.getName());
        verify(profileRepository).save(any(JobseekerProfile.class));
    }

    @Test
    void updateProfile_success() {
        JobseekerProfile existing = new JobseekerProfile();
        existing.setId(1L);
        existing.setUserId(100L);
        existing.setName("张三");
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(existing));
        when(profileRepository.save(any(JobseekerProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JobseekerProfileDto update = new JobseekerProfileDto();
        update.setBio("Senior Java dev");
        update.setExpectedPosition("Backend Engineer");
        update.setYearsOfExperience(5);

        JobseekerProfileDto result = profileService.updateProfile(100L, update);

        assertEquals("Senior Java dev", result.getBio());
        assertEquals("Backend Engineer", result.getExpectedPosition());
        assertEquals(5, result.getYearsOfExperience());
        verify(profileRepository).save(argThat(p ->
                "Senior Java dev".equals(p.getBio()) &&
                "Backend Engineer".equals(p.getExpectedPosition())
        ));
    }

    @Test
    void updateProfile_notFound_throwsException() {
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.empty());

        JobseekerProfileDto dto = new JobseekerProfileDto();
        assertThrows(BusinessException.class, () -> profileService.updateProfile(100L, dto));
    }

    @Test
    void getPublicProfile_anonymizesName() {
        JobseekerProfile profile = new JobseekerProfile();
        profile.setId(1L);
        profile.setUserId(100L);
        profile.setName("张三");
        profile.setBio("Java developer");
        profile.setExpectedPosition("Backend");
        profile.setYearsOfExperience(3);
        when(profileRepository.findByUserId(100L)).thenReturn(Optional.of(profile));

        JobseekerProfileDto dto = profileService.getPublicProfile(100L);

        assertEquals("张**", dto.getName());
        assertEquals("Java developer", dto.getBio());
        assertEquals("Backend", dto.getExpectedPosition());
        assertEquals(3, dto.getYearsOfExperience());
    }

    @Test
    void getPublicProfile_notFound_throwsException() {
        when(profileRepository.findByUserId(999L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> profileService.getPublicProfile(999L));
    }
}
