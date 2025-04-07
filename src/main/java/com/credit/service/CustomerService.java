package com.credit.service;

import com.credit.model.Customer;
import com.credit.repository.CustomerRepository;
import com.credit.exception.CustomerNotFoundException;
import com.credit.exception.DuplicateEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);
        
        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmail(customerDetails.getEmail());
        customer.setCreditScore(customerDetails.getCreditScore());
        customer.setAnnualSalary(customerDetails.getAnnualSalary());
        
        calculateCreditRiskScore(customer);
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    private void calculateCreditRiskScore(Customer customer) {
        double creditScoreWeight = 0.7;
        double salaryWeight = 0.3;
        
        double normalizedCreditScore = (customer.getCreditScore() - 300.0) / (850.0 - 300.0);
        
        double normalizedSalary = Math.min(customer.getAnnualSalary() / 200000.0, 1.0);
        
        double riskScore = 1.0 - ((normalizedCreditScore * creditScoreWeight) + (normalizedSalary * salaryWeight));
        
        customer.setCreditRiskScore(riskScore);
    }
} 