package com.findingjob.profile.repository;

import com.findingjob.profile.entity.JobseekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobseekerProfileRepository extends JpaRepository<JobseekerProfile, Long> {

    Optional<JobseekerProfile> findByUserId(Long userId);
}
