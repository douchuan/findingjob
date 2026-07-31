package com.findingjob.company.controller;

import com.findingjob.common.dto.ApiResponse;
import com.findingjob.common.enums.UserRole;
import com.findingjob.common.security.JwtUserPrincipal;
import com.findingjob.company.dto.CompanyDto;
import com.findingjob.company.dto.HRProfileDto;
import com.findingjob.company.entity.Company.VerificationStatus;
import com.findingjob.company.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@Tag(name = "Company", description = "Company management and public listing")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get my company info")
    public ApiResponse<CompanyDto> getMyCompany(@AuthenticationPrincipal JwtUserPrincipal principal) {
        CompanyDto dto = companyService.getMyCompany(principal.getUserId());
        return ApiResponse.success(dto);
    }

    @PutMapping("/me")
    @Operation(summary = "Update company info and upload license")
    public ApiResponse<CompanyDto> updateCompany(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody CompanyDto dto) {
        return ApiResponse.success(companyService.updateCompany(principal.getUserId(), dto));
    }

    @GetMapping("/me/hr-profile")
    @Operation(summary = "Get or create my HR profile")
    public ApiResponse<HRProfileDto> getMyHRProfile(@AuthenticationPrincipal JwtUserPrincipal principal) {
        String name = principal.getPhone() != null ? principal.getPhone() : "HR";
        return ApiResponse.success(companyService.getOrCreateHRProfile(principal.getUserId(), name));
    }

    @GetMapping("/public")
    @Operation(summary = "List approved companies (public)")
    public ApiResponse<Page<CompanyDto>> listCompanies(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<CompanyDto> result = companyService.listApprovedCompanies(
                industry, size, PageRequest.of(page, pageSize));
        return ApiResponse.success(result);
    }

    @GetMapping("/public/{id}")
    @Operation(summary = "Get company detail (public)")
    public ApiResponse<CompanyDto> getCompanyDetail(@PathVariable Long id) {
        return ApiResponse.success(companyService.getCompanyDetail(id));
    }

    @GetMapping("/admin/pending")
    @Operation(summary = "List pending companies (admin only)")
    public ApiResponse<Page<CompanyDto>> listPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        return ApiResponse.success(companyService.listCompaniesByStatus(
                VerificationStatus.PENDING, PageRequest.of(page, pageSize)));
    }

    @PostMapping("/admin/verify/{id}")
    @Operation(summary = "Approve or reject a company (admin only)")
    public ApiResponse<CompanyDto> verifyCompany(
            @PathVariable Long id,
            @RequestParam VerificationStatus status,
            @RequestParam(required = false) String comment) {
        return ApiResponse.success(companyService.verifyCompany(id, status, comment));
    }
}
