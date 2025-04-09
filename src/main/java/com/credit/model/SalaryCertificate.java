package com.credit.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_certificates")
@Data
public class SalaryCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String basicWage;
    private String housingAllowance;
    private String otherAllowance;
    private String fullWage;
    private String employerName;
    private String dateOfJoining;
    private String workingMonths;
    private String employmentStatus;
    private String salaryStartingDate;
    private String establishmentActivity;
    private String commercialRegistrationNumber;
    private String legalEntity;
    private String dateOfBirth;
    private String nationality;
    private String gosinumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 