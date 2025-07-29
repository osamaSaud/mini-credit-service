package com.credit.repository;

import com.credit.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    
    boolean existsByEmail(String email);
    
    // Dynamic filtering methods for better query capabilities
    List<CustomerEntity> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<CustomerEntity> findByLastNameContainingIgnoreCase(String lastName);
    
    List<CustomerEntity> findByCreditScoreBetween(Integer minScore, Integer maxScore);
    
    List<CustomerEntity> findByAnnualSalaryBetween(Double minSalary, Double maxSalary);
    
    @Query("SELECT c FROM CustomerEntity c WHERE " +
           "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:minCreditScore IS NULL OR c.creditScore >= :minCreditScore) AND " +
           "(:maxCreditScore IS NULL OR c.creditScore <= :maxCreditScore) AND " +
           "(:minSalary IS NULL OR c.annualSalary >= :minSalary) AND " +
           "(:maxSalary IS NULL OR c.annualSalary <= :maxSalary)")
    List<CustomerEntity> findCustomersWithFilters(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("minCreditScore") Integer minCreditScore,
            @Param("maxCreditScore") Integer maxCreditScore,
            @Param("minSalary") Double minSalary,
            @Param("maxSalary") Double maxSalary
    );
} 