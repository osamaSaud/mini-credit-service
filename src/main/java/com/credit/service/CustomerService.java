package com.credit.service;

import com.credit.model.Customer;
import com.credit.model.CustomerEvent;
import com.credit.repository.CustomerRepository;
import com.credit.exception.CustomerNotFoundException;
import com.credit.exception.DuplicateEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final MessagePublisherService messagePublisherService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, MessagePublisherService messagePublisherService) {
        this.customerRepository = customerRepository;
        this.messagePublisherService = messagePublisherService;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + customer.getEmail());
        }
        calculateCreditRiskScore(customer);
        Customer savedCustomer = customerRepository.save(customer);
        
        // Publish customer created event
        publishCustomerCreatedEvent(savedCustomer);
        
        return savedCustomer;
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);
        
        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmail(customerDetails.getEmail());
        customer.setCreditScore(customerDetails.getCreditScore());
        customer.setAnnualSalary(customerDetails.getAnnualSalary());
        
        calculateCreditRiskScore(customer);
        Customer updatedCustomer = customerRepository.save(customer);
        
        // Publish customer updated event
        publishCustomerUpdatedEvent(updatedCustomer);
        
        return updatedCustomer;
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
        
        // Publish customer deleted event
        publishCustomerDeletedEvent(id);
    }

    private void calculateCreditRiskScore(Customer customer) {
        double creditScoreWeight = 0.7;
        double salaryWeight = 0.3;
        
        double normalizedCreditScore = (customer.getCreditScore() - 300.0) / (850.0 - 300.0);
        
        double normalizedSalary = Math.min(customer.getAnnualSalary() / 200000.0, 1.0);
        
        double riskScore = 1.0 - ((normalizedCreditScore * creditScoreWeight) + (normalizedSalary * salaryWeight));
        
        customer.setCreditRiskScore(riskScore);
    }
    
    /**
     * Publish an event when a customer is created
     */
    private void publishCustomerCreatedEvent(Customer customer) {
        try {
            log.info("Publishing customer created event for customer ID: {}", customer.getId());
            CustomerEvent event = CustomerEvent.customerCreated(customer);
            messagePublisherService.publishCustomerEvent(event);
        } catch (Exception e) {
            log.error("Failed to publish customer created event", e);
        }
    }
    
    /**
     * Publish an event when a customer is updated
     */
    private void publishCustomerUpdatedEvent(Customer customer) {
        try {
            log.info("Publishing customer updated event for customer ID: {}", customer.getId());
            CustomerEvent event = CustomerEvent.customerUpdated(customer);
            messagePublisherService.publishCustomerEvent(event);
        } catch (Exception e) {
            log.error("Failed to publish customer updated event", e);
        }
    }
    
    /**
     * Publish an event when a customer is deleted
     */
    private void publishCustomerDeletedEvent(Long customerId) {
        try {
            log.info("Publishing customer deleted event for customer ID: {}", customerId);
            CustomerEvent event = CustomerEvent.customerDeleted(customerId);
            messagePublisherService.publishCustomerEvent(event);
        } catch (Exception e) {
            log.error("Failed to publish customer deleted event", e);
        }
    }
} 