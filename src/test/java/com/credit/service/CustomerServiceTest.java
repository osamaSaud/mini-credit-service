package com.credit.service;

import com.credit.model.Customer;
import com.credit.model.CustomerEvent;
import com.credit.repository.CustomerRepository;
import com.credit.exception.CustomerNotFoundException;
import com.credit.exception.DuplicateEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MessagePublisherService messagePublisherService;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setCreditScore(750);
        testCustomer.setAnnualSalary(80000.0);
    }

    @Test
    void getAllCustomers_ShouldReturnListOfCustomers() {
        // Arrange
        List<Customer> expectedCustomers = Arrays.asList(testCustomer);
        when(customerRepository.findAll()).thenReturn(expectedCustomers);

        // Act
        List<Customer> actualCustomers = customerService.getAllCustomers();

        // Assert
        assertEquals(expectedCustomers, actualCustomers);
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById_WhenCustomerExists_ShouldReturnCustomer() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        Customer foundCustomer = customerService.getCustomerById(1L);

        // Assert
        assertEquals(testCustomer, foundCustomer);
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_WhenCustomerDoesNotExist_ShouldThrowException() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
        verify(customerRepository).findById(1L);
    }

    @Test
    void createCustomer_WhenEmailIsUnique_ShouldCreateCustomer() {
        // Arrange
        when(customerRepository.existsByEmail(testCustomer.getEmail())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        Customer createdCustomer = customerService.createCustomer(testCustomer);

        // Assert
        assertNotNull(createdCustomer);
        assertEquals(testCustomer, createdCustomer);
        verify(customerRepository).existsByEmail(testCustomer.getEmail());
        verify(customerRepository).save(testCustomer);
        verify(messagePublisherService).publishCustomerEvent(any(CustomerEvent.class));
    }

    @Test
    void createCustomer_WhenEmailExists_ShouldThrowException() {
        // Arrange
        when(customerRepository.existsByEmail(testCustomer.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> customerService.createCustomer(testCustomer));
        verify(customerRepository).existsByEmail(testCustomer.getEmail());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_WhenCustomerExists_ShouldUpdateCustomer() {
        // Arrange
        Customer updatedDetails = new Customer();
        updatedDetails.setFirstName("Jane");
        updatedDetails.setLastName("Smith");
        updatedDetails.setEmail("jane.smith@example.com");
        updatedDetails.setCreditScore(800);
        updatedDetails.setAnnualSalary(90000.0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        Customer updatedCustomer = customerService.updateCustomer(1L, updatedDetails);

        // Assert
        assertNotNull(updatedCustomer);
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(any(Customer.class));
        verify(messagePublisherService).publishCustomerEvent(any(CustomerEvent.class));
    }

    @Test
    void deleteCustomer_WhenCustomerExists_ShouldDeleteCustomer() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository).existsById(1L);
        verify(customerRepository).deleteById(1L);
        verify(messagePublisherService).publishCustomerEvent(any(CustomerEvent.class));
    }

    @Test
    void deleteCustomer_WhenCustomerDoesNotExist_ShouldThrowException() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1L));
        verify(customerRepository).existsById(1L);
        verify(customerRepository, never()).deleteById(1L);
    }
} 