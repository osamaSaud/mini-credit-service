package com.credit.service;

import com.credit.dto.CreateCustomerRequest;
import com.credit.dto.UpdateCustomerRequest;
import com.credit.dto.CustomerDTO;
import com.credit.entity.CustomerEntity;
import com.credit.model.CustomerEvent;
import com.credit.repository.CustomerRepository;
import com.credit.mapper.CustomerMapper;
import com.credit.factory.CustomerEventFactory;
import com.credit.exception.CustomerNotFoundException;
import com.credit.exception.DuplicateEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final MessagePublisherService messagePublisherService;
    private final CustomerMapper customerMapper;
    private final CustomerEventFactory eventFactory;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, 
                          MessagePublisherService messagePublisherService,
                          CustomerMapper customerMapper,
                          CustomerEventFactory eventFactory) {
        this.customerRepository = customerRepository;
        this.messagePublisherService = messagePublisherService;
        this.customerMapper = customerMapper;
        this.eventFactory = eventFactory;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get customers with dynamic filtering
     * This demonstrates how to build flexible query capabilities
     */
    public List<CustomerDTO> getAllCustomers(String firstName, String lastName, 
                                           Integer minCreditScore, Integer maxCreditScore,
                                           Double minSalary, Double maxSalary) {
        
        // If no filters are provided, return all customers
        if (firstName == null && lastName == null && 
            minCreditScore == null && maxCreditScore == null && 
            minSalary == null && maxSalary == null) {
            return getAllCustomers();
        }
        
        // Use the dynamic filtering query from repository
        return customerRepository.findCustomersWithFilters(
                firstName, lastName, minCreditScore, maxCreditScore, minSalary, maxSalary)
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return customerMapper.toDTO(entity);
    }

    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }
        
        CustomerEntity entity = customerMapper.toEntity(request);
        calculateCreditRiskScore(entity);
        CustomerEntity savedEntity = customerRepository.save(entity);
        
        // Publish customer created event
        publishCustomerCreatedEvent(savedEntity);
        
        return customerMapper.toDTO(savedEntity);
    }

    public CustomerDTO updateCustomer(Long id, UpdateCustomerRequest request) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        customerMapper.updateEntity(entity, request);
        calculateCreditRiskScore(entity);
        CustomerEntity updatedEntity = customerRepository.save(entity);
        
        // Publish customer updated event
        publishCustomerUpdatedEvent(updatedEntity);
        
        return customerMapper.toDTO(updatedEntity);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
        
        // Publish customer deleted event
        publishCustomerDeletedEvent(id);
    }

    private void calculateCreditRiskScore(CustomerEntity entity) {
        double creditScoreWeight = 0.7;
        double salaryWeight = 0.3;
        
        double normalizedCreditScore = (entity.getCreditScore() - 300.0) / (850.0 - 300.0);
        
        double normalizedSalary = Math.min(entity.getAnnualSalary() / 200000.0, 1.0);
        
        double riskScore = 1.0 - ((normalizedCreditScore * creditScoreWeight) + (normalizedSalary * salaryWeight));
        
        entity.setCreditRiskScore(riskScore);
    }
    
    /**
     * Publish an event when a customer is created
     * Now using Factory pattern for event creation
     */
    private void publishCustomerCreatedEvent(CustomerEntity entity) {
        try {
            log.info("Publishing customer created event for customer ID: {}", entity.getId());
            CustomerEvent event = eventFactory.createCustomerCreatedEvent(entity);
            messagePublisherService.publishCustomerEvent(event);
        } catch (Exception e) {
            log.error("Failed to publish customer created event", e);
        }
    }
    
    /**
     * Publish an event when a customer is updated
     * Now using Factory pattern for intelligent event creation
     */
    private void publishCustomerUpdatedEvent(CustomerEntity entity) {
        try {
            log.info("Publishing customer updated event for customer ID: {}", entity.getId());
            // Use high-value customer factory method for better business logic
            CustomerEvent event = eventFactory.createHighValueCustomerEvent(entity);
            messagePublisherService.publishCustomerEvent(event);
        } catch (Exception e) {
            log.error("Failed to publish customer updated event", e);
        }
    }
    
    /**
     * Publish an event when a customer is deleted
     * Now using Factory pattern for consistent event creation
     */
    private void publishCustomerDeletedEvent(Long customerId) {
        try {
            log.info("Publishing customer deleted event for customer ID: {}", customerId);
            CustomerEvent event = eventFactory.createCustomerDeletedEvent(customerId);
            messagePublisherService.publishCustomerEvent(event);
        } catch (Exception e) {
            log.error("Failed to publish customer deleted event", e);
        }
    }
} 