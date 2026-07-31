package com.findingjob.resume.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import com.findingjob.resume.dto.ResumeRequestDto;
import com.findingjob.resume.entity.ResumeFile;
import com.findingjob.resume.entity.ResumeRequest;
import com.findingjob.resume.entity.ResumeRequest.RequestStatus;
import com.findingjob.resume.repository.ResumeFileRepository;
import com.findingjob.resume.repository.ResumeRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeFileRepository resumeFileRepository;
    private final ResumeRequestRepository resumeRequestRepository;

    @Value("${resume.max-size-mb:10}")
    private long maxSizeMb;

    @Value("${resume.download-expiry-hours:168}")
    private long downloadExpiryHours;

    public ResumeService(ResumeFileRepository resumeFileRepository,
                         ResumeRequestRepository resumeRequestRepository) {
        this.resumeFileRepository = resumeFileRepository;
        this.resumeRequestRepository = resumeRequestRepository;
    }

    /**
     * Upload resume (PDF only, max 10MB).
     */
    @Transactional
    public ResumeRequestDto uploadResume(Long userId, MultipartFile file) {
        if (!"application/pdf".equals(file.getContentType())) {
            throw new BusinessException(ErrorCode.FILE_TYPE_NOT_ALLOWED, "仅支持 PDF 格式");
        }
        if (file.getSize() > maxSizeMb * 1024 * 1024) {
            throw new BusinessException(ErrorCode.FILE_SIZE_EXCEEDED, "文件大小不能超过 " + maxSizeMb + "MB");
        }

        // In production, call storage-service to upload file
        String fileKey = "resumes/" + userId + "_" + System.currentTimeMillis() + ".pdf";

        ResumeFile resumeFile = resumeFileRepository.findByUserId(userId)
                .orElse(new ResumeFile());
        resumeFile.setUserId(userId);
        resumeFile.setFileKey(fileKey);
        resumeFile.setFileSize(file.getSize());
        resumeFile.setOriginalName(file.getOriginalFilename());
        resumeFileRepository.save(resumeFile);

        return null; // MVP: return nothing specific
    }

    /**
     * HR requests to view a jobseeker's resume.
     */
    @Transactional
    public ResumeRequestDto requestResume(Long hrId, Long jobseekerId) {
        // Check if already has an active request
        List<ResumeRequest> existing = resumeRequestRepository
                .findByHrIdAndJobseekerIdAndStatus(hrId, jobseekerId, RequestStatus.PENDING);
        if (!existing.isEmpty()) {
            throw new BusinessException(ErrorCode.DUPLICATE_ENTRY, "已存在待审批的请求");
        }

        ResumeRequest request = new ResumeRequest();
        request.setHrId(hrId);
        request.setJobseekerId(jobseekerId);
        request.setStatus(RequestStatus.PENDING);

        return toDto(resumeRequestRepository.save(request));
    }

    /**
     * Jobseeker approves/rejects a resume request.
     */
    @Transactional
    public ResumeRequestDto respondToRequest(Long requestId, Long jobseekerId, boolean approve) {
        ResumeRequest request = resumeRequestRepository.findById(requestId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "请求未找到"));

        if (!request.getJobseekerId().equals(jobseekerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作此请求");
        }

        if (approve) {
            request.setStatus(RequestStatus.APPROVED);
            request.setExpiresAt(LocalDateTime.now().plusHours(downloadExpiryHours));
            request.setDownloadUrl("/api/resume/download/" + requestId);
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }

        return toDto(resumeRequestRepository.save(request));
    }

    /**
     * Get pending requests for a jobseeker.
     */
    public List<ResumeRequestDto> getPendingRequests(Long jobseekerId) {
        return resumeRequestRepository.findByJobseekerIdOrderByCreatedAtDesc(jobseekerId).stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get requests made by an HR.
     */
    public List<ResumeRequestDto> getMyRequests(Long hrId) {
        return resumeRequestRepository.findByHrIdOrderByCreatedAtDesc(hrId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Check if HR has an approved request for a jobseeker.
     */
    public boolean hasApprovedRequest(Long hrId, Long jobseekerId) {
        return resumeRequestRepository.findByHrIdAndJobseekerIdAndStatus(
                hrId, jobseekerId, RequestStatus.APPROVED).stream()
                .anyMatch(r -> r.getExpiresAt() != null && r.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    private ResumeRequestDto toDto(ResumeRequest request) {
        ResumeRequestDto dto = new ResumeRequestDto();
        dto.setId(request.getId());
        dto.setHrId(request.getHrId());
        dto.setJobseekerId(request.getJobseekerId());
        dto.setStatus(request.getStatus().name());
        dto.setDownloadUrl(request.getDownloadUrl());
        dto.setExpiresAt(request.getExpiresAt());
        dto.setCreatedAt(request.getCreatedAt() != null ? request.getCreatedAt().toString() : null);
        return dto;
    }
}
