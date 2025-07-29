package com.credit.mapper;

import com.credit.dto.CreateCustomerRequest;
import com.credit.dto.UpdateCustomerRequest;
import com.credit.dto.CustomerDTO;
import com.credit.entity.CustomerEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for converting between DTOs and Entities
 * This demonstrates the Adapter pattern and keeps conversion logic centralized
 */
@Component
public class CustomerMapper {

    /**
     * Convert CustomerEntity to CustomerDTO for API responses
     */
    public CustomerDTO toDTO(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return CustomerDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .creditScore(entity.getCreditScore())
                .annualSalary(entity.getAnnualSalary())
                .creditRiskScore(entity.getCreditRiskScore())
                .build();
    }

    /**
     * Convert CreateCustomerRequest to CustomerEntity for persistence
     */
    public CustomerEntity toEntity(CreateCustomerRequest request) {
        if (request == null) {
            return null;
        }
        
        return CustomerEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .creditScore(request.getCreditScore())
                .annualSalary(request.getAnnualSalary())
                // Note: id and creditRiskScore are set by the service layer
                .build();
    }

    /**
     * Update existing CustomerEntity with data from UpdateCustomerRequest
     * This method demonstrates how to handle partial updates
     */
    public void updateEntity(CustomerEntity entity, UpdateCustomerRequest request) {
        if (entity == null || request == null) {
            return;
        }
        
        // Only update fields that are provided (not null)
        if (request.getFirstName() != null) {
            entity.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            entity.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            entity.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            entity.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getCreditScore() != null) {
            entity.setCreditScore(request.getCreditScore());
        }
        if (request.getAnnualSalary() != null) {
            entity.setAnnualSalary(request.getAnnualSalary());
        }
    }
} 