package com.credit.config;

import com.credit.model.Customer;
import com.credit.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    @Autowired
    public DataInitializer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        // Check if data already exists
        if (customerRepository.count() > 0) {
            System.out.println("Database already contains data. Skipping initialization.");
            return;
        }

        // Create sample customers with varying credit scores and salaries
        Customer customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setCreditScore(750);
        customer1.setAnnualSalary(85000.0);
        calculateCreditRiskScore(customer1);
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setEmail("jane.smith@example.com");
        customer2.setCreditScore(680);
        customer2.setAnnualSalary(65000.0);
        calculateCreditRiskScore(customer2);
        customerRepository.save(customer2);

        Customer customer3 = new Customer();
        customer3.setFirstName("Robert");
        customer3.setLastName("Johnson");
        customer3.setEmail("robert.johnson@example.com");
        customer3.setCreditScore(620);
        customer3.setAnnualSalary(55000.0);
        calculateCreditRiskScore(customer3);
        customerRepository.save(customer3);

        Customer customer4 = new Customer();
        customer4.setFirstName("Emily");
        customer4.setLastName("Williams");
        customer4.setEmail("emily.williams@example.com");
        customer4.setCreditScore(800);
        customer4.setAnnualSalary(120000.0);
        calculateCreditRiskScore(customer4);
        customerRepository.save(customer4);

        Customer customer5 = new Customer();
        customer5.setFirstName("Michael");
        customer5.setLastName("Brown");
        customer5.setEmail("michael.brown@example.com");
        customer5.setCreditScore(550);
        customer5.setAnnualSalary(45000.0);
        calculateCreditRiskScore(customer5);
        customerRepository.save(customer5);

        System.out.println("Sample data initialized with 5 customers");
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