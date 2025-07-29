package com.credit.config;

import com.credit.entity.CustomerEntity;
import com.credit.builder.CustomerEntityBuilder;
import com.credit.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer that creates sample customers for testing and demonstration
 * Now uses the new CustomerEntity and Builder pattern
 */
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

        // Create sample customers using Builder pattern - much cleaner!
        CustomerEntity customer1 = CustomerEntityBuilder.builder()
                .withFirstName("John")
                .withLastName("Doe")
                .withEmail("john.doe@example.com")
                .withCreditScore(750)
                .withAnnualSalary(85000.0)
                .withPhoneNumber("+1-555-0101")
                .build();
        calculateCreditRiskScore(customer1);
        customerRepository.save(customer1);

        CustomerEntity customer2 = CustomerEntityBuilder.builder()
                .withFirstName("Jane")
                .withLastName("Smith")
                .withEmail("jane.smith@example.com")
                .withCreditScore(680)
                .withAnnualSalary(65000.0)
                .withPhoneNumber("+1-555-0102")
                .build();
        calculateCreditRiskScore(customer2);
        customerRepository.save(customer2);

        CustomerEntity customer3 = CustomerEntityBuilder.builder()
                .withFirstName("Robert")
                .withLastName("Johnson")
                .withEmail("robert.johnson@example.com")
                .withCreditScore(620)
                .withAnnualSalary(55000.0)
                .withPhoneNumber("+1-555-0103")
                .build();
        calculateCreditRiskScore(customer3);
        customerRepository.save(customer3);

        // Using convenience method for high-value customer
        CustomerEntity customer4 = CustomerEntityBuilder.builder()
                .withFirstName("Emily")
                .withLastName("Williams")
                .withEmail("emily.williams@example.com")
                .asHighValueCustomer()  // Sets good credit score and high salary
                .withPhoneNumber("+1-555-0104")
                .build();
        calculateCreditRiskScore(customer4);
        customerRepository.save(customer4);

        CustomerEntity customer5 = CustomerEntityBuilder.builder()
                .withFirstName("Michael")
                .withLastName("Brown")
                .withEmail("michael.brown@example.com")
                .withCreditScore(550)
                .withAnnualSalary(45000.0)
                .withPhoneNumber("+1-555-0105")
                .build();
        calculateCreditRiskScore(customer5);
        customerRepository.save(customer5);

        System.out.println("Sample data initialized with 5 customers using Builder pattern");
    }
    
    private void calculateCreditRiskScore(CustomerEntity customer) {
        double creditScoreWeight = 0.7;
        double salaryWeight = 0.3;
        
        double normalizedCreditScore = (customer.getCreditScore() - 300.0) / (850.0 - 300.0);
        
        double normalizedSalary = Math.min(customer.getAnnualSalary() / 200000.0, 1.0);
        
        double riskScore = 1.0 - ((normalizedCreditScore * creditScoreWeight) + (normalizedSalary * salaryWeight));
        
        customer.setCreditRiskScore(riskScore);
    }
} 