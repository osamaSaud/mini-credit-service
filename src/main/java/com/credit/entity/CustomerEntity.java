package com.credit.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Entity for Customer data persistence
 * This class is responsible ONLY for database operations and structure
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "annual_salary", nullable = false)
    private Double annualSalary;

    @Column(name = "credit_risk_score")
    private Double creditRiskScore;
    
    // Note: We'll handle the relationship with SalaryCertificate separately
    // This keeps the entity focused on its core responsibility
    
    @Override
    public String toString() {
        return "CustomerEntity{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", creditScore=" + creditScore +
                ", annualSalary=" + annualSalary +
                ", creditRiskScore=" + creditRiskScore +
                '}';
    }
} 