package com.credit.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_certificates")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SalaryCertificate implements Serializable {
    private static final long serialVersionUID = 1L;
    
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

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("salaryCertificates")
    private Customer customer;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "SalaryCertificate{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", employerName='" + employerName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 