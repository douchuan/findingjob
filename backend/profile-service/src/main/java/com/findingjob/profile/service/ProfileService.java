package com.findingjob.profile.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import com.findingjob.profile.dto.JobseekerProfileDto;
import com.findingjob.profile.entity.JobseekerProfile;
import com.findingjob.profile.repository.JobseekerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final JobseekerProfileRepository profileRepository;

    public ProfileService(JobseekerProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Get or create a jobseeker profile for the given user.
     * If no profile exists, creates an empty one.
     */
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

    /**
     * Update the jobseeker profile. Only the profile owner can update.
     */
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

    /**
     * Get public profile info (L1) for any viewer.
     * Returns anonymized name (张**).
     */
    public JobseekerProfileDto getPublicProfile(Long userId) {
        JobseekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Profile not found"));

        JobseekerProfileDto dto = toDto(profile);
        // Anonymize name
        String name = dto.getName();
        if (name != null && name.length() > 1) {
            dto.setName(name.charAt(0) + "**");
        }
        return dto;
    }

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
}
