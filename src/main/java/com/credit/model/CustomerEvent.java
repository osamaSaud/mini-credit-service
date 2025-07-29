package com.credit.model;

import com.credit.entity.CustomerEntity;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an event related to a customer
 * This is the message that will be sent to RabbitMQ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    
    // Type of event (e.g., CREATED, UPDATED, DELETED)
    private EventType eventType;
    
    // The customer ID this event is related to
    private Long customerId;
    
    // When the event occurred - using String to avoid serialization issues
    private String timestamp;
    
    // Any additional information about the event
    private String message;
    
    // Basic customer details for the event instead of full customer object
    private Map<String, Object> customerDetails;
    
    /**
     * Event types for customer-related events
     */
    public enum EventType {
        CREATED,
        UPDATED,
        DELETED,
        CREDIT_SCORE_UPDATED
    }
    
    /**
     * Create a new customer created event
     */
    public static CustomerEvent customerCreated(CustomerEntity customer) {
        Map<String, Object> details = extractCustomerDetails(customer);
        
        return CustomerEvent.builder()
                .eventType(EventType.CREATED)
                .customerId(customer.getId())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .message("Customer created")
                .customerDetails(details)
                .build();
    }
    
    /**
     * Create a new customer updated event
     */
    public static CustomerEvent customerUpdated(CustomerEntity customer) {
        Map<String, Object> details = extractCustomerDetails(customer);
        
        return CustomerEvent.builder()
                .eventType(EventType.UPDATED)
                .customerId(customer.getId())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .message("Customer updated")
                .customerDetails(details)
                .build();
    }
    
    /**
     * Create a new customer deleted event
     */
    public static CustomerEvent customerDeleted(Long customerId) {
        return CustomerEvent.builder()
                .eventType(EventType.DELETED)
                .customerId(customerId)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .message("Customer deleted")
                .build();
    }
    
    /**
     * Extract basic customer details to avoid serialization issues
     */
    private static Map<String, Object> extractCustomerDetails(CustomerEntity customer) {
        Map<String, Object> details = new HashMap<>();
        details.put("id", customer.getId());
        details.put("firstName", customer.getFirstName());
        details.put("lastName", customer.getLastName());
        details.put("email", customer.getEmail());
        details.put("creditScore", customer.getCreditScore());
        details.put("annualSalary", customer.getAnnualSalary());
        details.put("creditRiskScore", customer.getCreditRiskScore());
        return details;
    }
} 