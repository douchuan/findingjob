package com.findingjob.company.repository;

import com.findingjob.company.entity.Company;
import com.findingjob.company.entity.Company.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Page<Company> findByVerificationStatus(VerificationStatus status, Pageable pageable);

    List<Company> findByVerificationStatus(VerificationStatus status);

    Page<Company> findByVerificationStatusAndIndustry(VerificationStatus status, String industry, Pageable pageable);

    Page<Company> findByVerificationStatusAndSize(VerificationStatus status, String size, Pageable pageable);
}
