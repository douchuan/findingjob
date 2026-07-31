package com.findingjob.company.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.company.dto.CompanyDto;
import com.findingjob.company.entity.Company;
import com.findingjob.company.entity.Company.VerificationStatus;
import com.findingjob.company.entity.HRProfile;
import com.findingjob.company.repository.CompanyRepository;
import com.findingjob.company.repository.HRProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private HRProfileRepository hrProfileRepository;

    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(companyRepository, hrProfileRepository);
    }

    @Test
    void getOrCreateHRProfile_existing_returnsDto() {
        HRProfile existing = new HRProfile();
        existing.setId(1L);
        existing.setUserId(100L);
        when(hrProfileRepository.findByUserId(100L)).thenReturn(Optional.of(existing));

        var dto = companyService.getOrCreateHRProfile(100L, "HR");

        assertEquals(1L, dto.getId());
        assertEquals(100L, dto.getUserId());
        verify(hrProfileRepository, never()).save(any());
    }

    @Test
    void getOrCreateHRProfile_new_createsProfile() {
        when(hrProfileRepository.findByUserId(100L)).thenReturn(Optional.empty());
        when(hrProfileRepository.save(any(HRProfile.class))).thenAnswer(inv -> {
            HRProfile p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        var dto = companyService.getOrCreateHRProfile(100L, "新HR");

        assertEquals(1L, dto.getId());
        verify(hrProfileRepository).save(any(HRProfile.class));
    }

    @Test
    void updateCompany_newCompany_createsWithPending() {
        HRProfile hrProfile = new HRProfile();
        hrProfile.setId(1L);
        hrProfile.setUserId(100L);
        when(hrProfileRepository.findByUserId(100L)).thenReturn(Optional.of(hrProfile));
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> {
            Company c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        CompanyDto dto = new CompanyDto();
        dto.setName("Test Corp");
        dto.setIndustry("互联网");
        dto.setSize("50-200");

        CompanyDto result = companyService.updateCompany(100L, dto);

        assertEquals("Test Corp", result.getName());
        assertEquals("PENDING", result.getVerificationStatus());
        verify(companyRepository).save(argThat(c ->
                "Test Corp".equals(c.getName()) &&
                c.getVerificationStatus() == VerificationStatus.PENDING
        ));
        // HR profile linked to company
        assertEquals(1L, hrProfile.getCompanyId());
    }

    @Test
    void updateCompany_reUploadLicense_resetsToPending() {
        HRProfile hrProfile = new HRProfile();
        hrProfile.setId(1L);
        hrProfile.setUserId(100L);
        hrProfile.setCompanyId(1L);

        Company existing = new Company();
        existing.setId(1L);
        existing.setName("Test Corp");
        existing.setVerificationStatus(VerificationStatus.REJECTED);

        when(hrProfileRepository.findByUserId(100L)).thenReturn(Optional.of(hrProfile));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        CompanyDto update = new CompanyDto();
        update.setName("Test Corp");
        update.setLicenseFileKey("licenses/new-license.jpg");

        CompanyDto result = companyService.updateCompany(100L, update);

        assertEquals("PENDING", result.getVerificationStatus());
    }

    @Test
    void getMyCompany_noCompany_returnsNull() {
        HRProfile hrProfile = new HRProfile();
        hrProfile.setId(1L);
        hrProfile.setUserId(100L);
        when(hrProfileRepository.findByUserId(100L)).thenReturn(Optional.of(hrProfile));

        CompanyDto result = companyService.getMyCompany(100L);

        assertNull(result);
    }

    @Test
    void listApprovedCompanies_returnsOnlyApproved() {
        Company approved = new Company();
        approved.setId(1L);
        approved.setName("Approved Corp");
        approved.setVerificationStatus(VerificationStatus.APPROVED);

        when(companyRepository.findByVerificationStatus(eq(VerificationStatus.APPROVED), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(approved)));

        Page<CompanyDto> result = companyService.listApprovedCompanies(null, null, PageRequest.of(0, 20));

        assertEquals(1, result.getTotalElements());
        assertEquals("Approved Corp", result.getContent().get(0).getName());
    }

    @Test
    void verifyCompany_approve() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Test Corp");
        company.setVerificationStatus(VerificationStatus.PENDING);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        CompanyDto result = companyService.verifyCompany(1L, VerificationStatus.APPROVED, "资质齐全");

        assertEquals("APPROVED", result.getVerificationStatus());
        assertEquals("资质齐全", result.getReviewComment());
    }

    @Test
    void getMyCompany_hrProfileNotFound_throwsException() {
        when(hrProfileRepository.findByUserId(999L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> companyService.getMyCompany(999L));
    }
}
