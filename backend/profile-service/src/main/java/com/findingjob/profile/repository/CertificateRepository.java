package com.findingjob.profile.repository;

import com.findingjob.profile.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByProfileIdOrderByIssueDateDesc(Long profileId);
}
