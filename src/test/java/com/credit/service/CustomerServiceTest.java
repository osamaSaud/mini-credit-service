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

/**
 * Updated test class for CustomerService using new DTO/Entity architecture
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MessagePublisherService messagePublisherService;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private CustomerEventFactory eventFactory;

    @InjectMocks
    private CustomerService customerService;

    private CustomerEntity testEntity;
    private CustomerDTO testDTO;
    private CreateCustomerRequest createRequest;
    private UpdateCustomerRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Setup test entity
        testEntity = new CustomerEntity();
        testEntity.setId(1L);
        testEntity.setFirstName("John");
        testEntity.setLastName("Doe");
        testEntity.setEmail("john.doe@example.com");
        testEntity.setCreditScore(750);
        testEntity.setAnnualSalary(80000.0);

        // Setup test DTO
        testDTO = CustomerDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .creditScore(750)
                .annualSalary(80000.0)
                .build();

        // Setup create request
        createRequest = new CreateCustomerRequest();
        createRequest.setFirstName("John");
        createRequest.setLastName("Doe");
        createRequest.setEmail("john.doe@example.com");
        createRequest.setCreditScore(750);
        createRequest.setAnnualSalary(80000.0);

        // Setup update request
        updateRequest = new UpdateCustomerRequest();
        updateRequest.setFirstName("Jane");
        updateRequest.setCreditScore(800);
    }

    @Test
    void getAllCustomers_ShouldReturnListOfCustomerDTOs() {
        // Arrange
        List<CustomerEntity> entities = Arrays.asList(testEntity);
        when(customerRepository.findAll()).thenReturn(entities);
        when(customerMapper.toDTO(testEntity)).thenReturn(testDTO);

        // Act
        List<CustomerDTO> actualCustomers = customerService.getAllCustomers();

        // Assert
        assertEquals(1, actualCustomers.size());
        assertEquals(testDTO, actualCustomers.get(0));
        verify(customerRepository).findAll();
        verify(customerMapper).toDTO(testEntity);
    }

    @Test
    void getCustomerById_WhenCustomerExists_ShouldReturnCustomerDTO() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(customerMapper.toDTO(testEntity)).thenReturn(testDTO);

        // Act
        CustomerDTO foundCustomer = customerService.getCustomerById(1L);

        // Assert
        assertEquals(testDTO, foundCustomer);
        verify(customerRepository).findById(1L);
        verify(customerMapper).toDTO(testEntity);
    }

    @Test
    void getCustomerById_WhenCustomerDoesNotExist_ShouldThrowException() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
        verify(customerRepository).findById(1L);
        verify(customerMapper, never()).toDTO(any());
    }

    @Test
    void createCustomer_WhenEmailIsUnique_ShouldCreateCustomer() {
        // Arrange
        when(customerRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(customerMapper.toEntity(createRequest)).thenReturn(testEntity);
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(testEntity);
        when(customerMapper.toDTO(testEntity)).thenReturn(testDTO);
        when(eventFactory.createCustomerCreatedEvent(testEntity)).thenReturn(mock(CustomerEvent.class));

        // Act
        CustomerDTO createdCustomer = customerService.createCustomer(createRequest);

        // Assert
        assertNotNull(createdCustomer);
        assertEquals(testDTO, createdCustomer);
        verify(customerRepository).existsByEmail(createRequest.getEmail());
        verify(customerMapper).toEntity(createRequest);
        verify(customerRepository).save(any(CustomerEntity.class));
        verify(customerMapper).toDTO(testEntity);
        verify(eventFactory).createCustomerCreatedEvent(testEntity);
        verify(messagePublisherService).publishCustomerEvent(any(CustomerEvent.class));
    }

    @Test
    void createCustomer_WhenEmailExists_ShouldThrowException() {
        // Arrange
        when(customerRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> customerService.createCustomer(createRequest));
        verify(customerRepository).existsByEmail(createRequest.getEmail());
        verify(customerRepository, never()).save(any(CustomerEntity.class));
        verify(messagePublisherService, never()).publishCustomerEvent(any());
    }

    @Test
    void updateCustomer_WhenCustomerExists_ShouldUpdateCustomer() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(testEntity);
        when(customerMapper.toDTO(testEntity)).thenReturn(testDTO);
        when(eventFactory.createHighValueCustomerEvent(testEntity)).thenReturn(mock(CustomerEvent.class));

        // Act
        CustomerDTO updatedCustomer = customerService.updateCustomer(1L, updateRequest);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(testDTO, updatedCustomer);
        verify(customerRepository).findById(1L);
        verify(customerMapper).updateEntity(testEntity, updateRequest);
        verify(customerRepository).save(testEntity);
        verify(customerMapper).toDTO(testEntity);
        verify(eventFactory).createHighValueCustomerEvent(testEntity);
        verify(messagePublisherService).publishCustomerEvent(any(CustomerEvent.class));
    }

    @Test
    void deleteCustomer_WhenCustomerExists_ShouldDeleteCustomer() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(eventFactory.createCustomerDeletedEvent(1L)).thenReturn(mock(CustomerEvent.class));

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository).existsById(1L);
        verify(customerRepository).deleteById(1L);
        verify(eventFactory).createCustomerDeletedEvent(1L);
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
        verify(messagePublisherService, never()).publishCustomerEvent(any());
    }

    @Test
    void getAllCustomersWithFilters_ShouldReturnFilteredResults() {
        // Arrange
        List<CustomerEntity> entities = Arrays.asList(testEntity);
        when(customerRepository.findCustomersWithFilters(
                "John", null, 700, null, null, null)).thenReturn(entities);
        when(customerMapper.toDTO(testEntity)).thenReturn(testDTO);

        // Act
        List<CustomerDTO> results = customerService.getAllCustomers(
                "John", null, 700, null, null, null);

        // Assert
        assertEquals(1, results.size());
        assertEquals(testDTO, results.get(0));
        verify(customerRepository).findCustomersWithFilters(
                "John", null, 700, null, null, null);
        verify(customerMapper).toDTO(testEntity);
    }
} 