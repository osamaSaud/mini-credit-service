package com.credit.builder;

import com.credit.entity.CustomerEntity;

/**
 * Builder Pattern Implementation for CustomerEntity
 * 
 * Why use Builder Pattern?
 * 1. Makes object construction more readable
 * 2. Allows step-by-step construction
 * 3. Prevents inconsistent object states
 * 4. Makes complex construction simple
 * 
 * This demonstrates the Builder Pattern (one of the Gang of Four creational patterns)
 */
public class CustomerEntityBuilder {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer creditScore;
    private Double annualSalary;
    private Double creditRiskScore;
    
    // Private constructor to force use of static factory method
    private CustomerEntityBuilder() {}
    
    /**
     * Static factory method to start building
     * This follows the Builder pattern convention
     */
    public static CustomerEntityBuilder builder() {
        return new CustomerEntityBuilder();
    }
    
    /**
     * Fluent interface methods for setting properties
     * Each method returns 'this' to enable method chaining
     */
    public CustomerEntityBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    
    public CustomerEntityBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    
    public CustomerEntityBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    
    public CustomerEntityBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
    
    public CustomerEntityBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    
    public CustomerEntityBuilder withCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
        return this;
    }
    
    public CustomerEntityBuilder withAnnualSalary(Double annualSalary) {
        this.annualSalary = annualSalary;
        return this;
    }
    
    public CustomerEntityBuilder withCreditRiskScore(Double creditRiskScore) {
        this.creditRiskScore = creditRiskScore;
        return this;
    }
    
    /**
     * Convenience method for setting full name at once
     * This shows how builders can provide helpful abstractions
     */
    public CustomerEntityBuilder withFullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        return this;
    }
    
    /**
     * Method to set customer as high-value (good credit + high salary)
     * This demonstrates how builders can encapsulate business logic
     */
    public CustomerEntityBuilder asHighValueCustomer() {
        this.creditScore = 800; // Excellent credit
        this.annualSalary = 150000.0; // High salary
        return this;
    }
    
    /**
     * Method to set customer as standard profile
     */
    public CustomerEntityBuilder asStandardCustomer() {
        this.creditScore = 650; // Fair credit
        this.annualSalary = 50000.0; // Average salary
        return this;
    }
    
    /**
     * Build method that creates the final object
     * This is where validation can occur
     */
    public CustomerEntity build() {
        // Validation logic (example of fail-fast behavior)
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalStateException("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalStateException("Last name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalStateException("Email is required");
        }
        
        // Create and return the entity
        CustomerEntity entity = new CustomerEntity();
        entity.setId(id);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setPhoneNumber(phoneNumber);
        entity.setCreditScore(creditScore);
        entity.setAnnualSalary(annualSalary);
        entity.setCreditRiskScore(creditRiskScore);
        
        return entity;
    }
} 