package com.findingjob.company.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import com.findingjob.company.dto.CompanyDto;
import com.findingjob.company.dto.HRProfileDto;
import com.findingjob.company.entity.Company;
import com.findingjob.company.entity.Company.VerificationStatus;
import com.findingjob.company.entity.HRProfile;
import com.findingjob.company.repository.CompanyRepository;
import com.findingjob.company.repository.HRProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final HRProfileRepository hrProfileRepository;

    public CompanyService(CompanyRepository companyRepository, HRProfileRepository hrProfileRepository) {
        this.companyRepository = companyRepository;
        this.hrProfileRepository = hrProfileRepository;
    }

    /**
     * Get or create HR profile and company for the user.
     */
    @Transactional
    public HRProfileDto getOrCreateHRProfile(Long userId, String name) {
        return hrProfileRepository.findByUserId(userId)
                .map(this::toHRDto)
                .orElseGet(() -> {
                    HRProfile hrProfile = new HRProfile();
                    hrProfile.setUserId(userId);
                    hrProfile.setPhone(name);
                    HRProfile saved = hrProfileRepository.save(hrProfile);
                    return toHRDto(saved);
                });
    }

    /**
     * Update company info and upload business license.
     */
    @Transactional
    public CompanyDto updateCompany(Long userId, CompanyDto dto) {
        HRProfile hrProfile = hrProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "HR profile not found"));

        Company company;
        if (hrProfile.getCompanyId() != null) {
            company = companyRepository.findById(hrProfile.getCompanyId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Company not found"));
        } else {
            company = new Company();
            company.setVerificationStatus(VerificationStatus.PENDING);
        }

        company.setName(dto.getName());
        company.setIndustry(dto.getIndustry());
        company.setSize(dto.getSize());
        company.setDescription(dto.getDescription());
        company.setLogoUrl(dto.getLogoUrl());

        if (dto.getLicenseFileKey() != null) {
            company.setLicenseFileKey(dto.getLicenseFileKey());
            // Reset to pending when re-uploading license
            if (company.getVerificationStatus() == VerificationStatus.REJECTED) {
                company.setVerificationStatus(VerificationStatus.PENDING);
            }
        }

        Company saved = companyRepository.save(company);

        if (hrProfile.getCompanyId() == null) {
            hrProfile.setCompanyId(saved.getId());
            hrProfileRepository.save(hrProfile);
        }

        return toDto(saved);
    }

    /**
     * Get company info for the current HR.
     */
    public CompanyDto getMyCompany(Long userId) {
        HRProfile hrProfile = hrProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "HR profile not found"));

        if (hrProfile.getCompanyId() == null) {
            return null;
        }

        return companyRepository.findById(hrProfile.getCompanyId())
                .map(this::toDto)
                .orElse(null);
    }

    /**
     * List approved companies (public view).
     */
    public Page<CompanyDto> listApprovedCompanies(String industry, String size, Pageable pageable) {
        if (industry != null && !industry.isEmpty()) {
            return companyRepository.findByVerificationStatusAndIndustry(VerificationStatus.APPROVED, industry, pageable)
                    .map(this::toDto);
        }
        if (size != null && !size.isEmpty()) {
            return companyRepository.findByVerificationStatusAndSize(VerificationStatus.APPROVED, size, pageable)
                    .map(this::toDto);
        }
        return companyRepository.findByVerificationStatus(VerificationStatus.APPROVED, pageable)
                .map(this::toDto);
    }

    /**
     * Get company detail (public).
     */
    public CompanyDto getCompanyDetail(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Company not found"));
        return toDto(company);
    }

    /**
     * Admin: list companies by verification status.
     */
    public Page<CompanyDto> listCompaniesByStatus(VerificationStatus status, Pageable pageable) {
        return companyRepository.findByVerificationStatus(status, pageable)
                .map(this::toDto);
    }

    /**
     * Admin: approve or reject a company.
     */
    @Transactional
    public CompanyDto verifyCompany(Long companyId, VerificationStatus status, String comment) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Company not found"));

        company.setVerificationStatus(status);
        company.setReviewComment(comment);
        return toDto(companyRepository.save(company));
    }

    private CompanyDto toDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setIndustry(company.getIndustry());
        dto.setSize(company.getSize());
        dto.setDescription(company.getDescription());
        dto.setLogoUrl(company.getLogoUrl());
        dto.setVerificationStatus(company.getVerificationStatus().name());
        dto.setLicenseFileKey(company.getLicenseFileKey());
        dto.setReviewComment(company.getReviewComment());
        return dto;
    }

    private HRProfileDto toHRDto(HRProfile hrProfile) {
        HRProfileDto dto = new HRProfileDto();
        dto.setId(hrProfile.getId());
        dto.setUserId(hrProfile.getUserId());
        dto.setCompanyId(hrProfile.getCompanyId());
        dto.setPosition(hrProfile.getPosition());
        dto.setPhone(hrProfile.getPhone());
        return dto;
    }
}
