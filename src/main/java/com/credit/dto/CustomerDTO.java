package com.credit.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Customer API responses
 * This class defines how customer data is presented to API consumers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer creditScore;
    private Double annualSalary;
    private Double creditRiskScore;
    
    // Full name computed property - this is an example of how DTOs can provide
    // computed values without storing them in the database
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    // Credit rating computed property - business logic that doesn't belong in the entity
    public String getCreditRating() {
        if (creditScore == null) return "Not Rated";
        if (creditScore >= 750) return "Excellent";
        if (creditScore >= 700) return "Good";
        if (creditScore >= 650) return "Fair";
        if (creditScore >= 600) return "Poor";
        return "Very Poor";
    }
} 