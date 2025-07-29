package com.credit.factory;

import com.credit.entity.CustomerEntity;
import com.credit.model.CustomerEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory Pattern Implementation for CustomerEvent creation
 * 
 * Why use Factory Pattern?
 * 1. Centralizes object creation logic
 * 2. Provides a single point for creation policies
 * 3. Makes code more maintainable and testable
 * 4. Allows for easy extension of creation logic
 * 
 * This demonstrates the Factory Pattern (another Gang of Four creational pattern)
 */
@Component
public class CustomerEventFactory {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    
    /**
     * Create customer created event with standard messaging
     */
    public CustomerEvent createCustomerCreatedEvent(CustomerEntity entity) {
        return createEvent(entity, CustomerEvent.EventType.CREATED, "New customer registered");
    }
    
    /**
     * Create customer updated event with standard messaging
     */
    public CustomerEvent createCustomerUpdatedEvent(CustomerEntity entity) {
        return createEvent(entity, CustomerEvent.EventType.UPDATED, "Customer information updated");
    }
    
    /**
     * Create customer deleted event
     */
    public CustomerEvent createCustomerDeletedEvent(Long customerId) {
        return CustomerEvent.builder()
                .eventType(CustomerEvent.EventType.DELETED)
                .customerId(customerId)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .message("Customer account deleted")
                .build();
    }
    
    /**
     * Create specialized event for credit score updates
     * This shows how Factory can create different variants
     */
    public CustomerEvent createCreditScoreUpdatedEvent(CustomerEntity entity, Integer oldScore) {
        String message = String.format("Credit score updated from %d to %d", 
                                      oldScore, entity.getCreditScore());
        
        Map<String, Object> details = extractCustomerDetails(entity);
        details.put("previousCreditScore", oldScore);
        details.put("scoreChange", entity.getCreditScore() - oldScore);
        
        return CustomerEvent.builder()
                .eventType(CustomerEvent.EventType.CREDIT_SCORE_UPDATED)
                .customerId(entity.getId())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .message(message)
                .customerDetails(details)
                .build();
    }
    
    /**
     * Create event for high-value customer (example of business-specific factory method)
     */
    public CustomerEvent createHighValueCustomerEvent(CustomerEntity entity) {
        if (entity.getCreditScore() >= 750 && entity.getAnnualSalary() >= 100000) {
            String message = "High-value customer identified - Consider premium services";
            return createEvent(entity, CustomerEvent.EventType.UPDATED, message);
        }
        return createCustomerUpdatedEvent(entity); // Fallback to standard event
    }
    
    /**
     * Central event creation method - demonstrates how Factory centralizes logic
     */
    private CustomerEvent createEvent(CustomerEntity entity, CustomerEvent.EventType eventType, String message) {
        Map<String, Object> details = extractCustomerDetails(entity);
        
        return CustomerEvent.builder()
                .eventType(eventType)
                .customerId(entity.getId())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .message(message)
                .customerDetails(details)
                .build();
    }
    
    /**
     * Extract customer details for event - this could be enhanced with business rules
     */
    private Map<String, Object> extractCustomerDetails(CustomerEntity entity) {
        Map<String, Object> details = new HashMap<>();
        details.put("id", entity.getId());
        details.put("firstName", entity.getFirstName());
        details.put("lastName", entity.getLastName());
        details.put("email", entity.getEmail());
        details.put("creditScore", entity.getCreditScore());
        details.put("annualSalary", entity.getAnnualSalary());
        details.put("creditRiskScore", entity.getCreditRiskScore());
        
        // Add computed fields that might be useful for event consumers
        details.put("fullName", entity.getFirstName() + " " + entity.getLastName());
        details.put("isHighValue", entity.getCreditScore() >= 750 && entity.getAnnualSalary() >= 100000);
        
        return details;
    }
} 