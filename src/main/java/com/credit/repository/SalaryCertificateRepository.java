package com.credit.repository;

import com.credit.model.SalaryCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryCertificateRepository extends JpaRepository<SalaryCertificate, Long> {
} 