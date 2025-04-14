package com.credit.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score cannot exceed 850")
    private Integer creditScore;

    @NotNull(message = "Annual salary is required")
    @Min(value = 0, message = "Annual salary cannot be negative")
    private Double annualSalary;

    private Double creditRiskScore;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<SalaryCertificate> salaryCertificates;
} 