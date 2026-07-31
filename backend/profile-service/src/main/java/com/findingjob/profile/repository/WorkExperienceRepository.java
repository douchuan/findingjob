package com.findingjob.profile.repository;

import com.findingjob.profile.entity.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {

    List<WorkExperience> findByProfileIdOrderByStartDateDesc(Long profileId);
}
