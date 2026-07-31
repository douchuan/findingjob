package com.findingjob.resume.repository;

import com.findingjob.resume.entity.ResumeRequest;
import com.findingjob.resume.entity.ResumeRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRequestRepository extends JpaRepository<ResumeRequest, Long> {

    List<ResumeRequest> findByJobseekerIdOrderByCreatedAtDesc(Long jobseekerId);

    List<ResumeRequest> findByHrIdOrderByCreatedAtDesc(Long hrId);

    Optional<ResumeRequest> findByHrIdAndJobseekerId(Long hrId, Long jobseekerId);

    List<ResumeRequest> findByHrIdAndJobseekerIdAndStatus(Long hrId, Long jobseekerId, RequestStatus status);
}
