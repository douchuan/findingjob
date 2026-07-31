package com.findingjob.resume.repository;

import com.findingjob.resume.entity.ResumeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeFileRepository extends JpaRepository<ResumeFile, Long> {

    Optional<ResumeFile> findByUserId(Long userId);
}
