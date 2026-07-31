package com.findingjob.company.repository;

import com.findingjob.company.entity.HRProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HRProfileRepository extends JpaRepository<HRProfile, Long> {

    Optional<HRProfile> findByUserId(Long userId);
}
