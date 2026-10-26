package com.carrental.app.service;

import com.carrental.app.dto.request.CustomerRequestDTO;
import com.carrental.app.dto.response.CustomerResponseDTO;
import com.carrental.app.entity.Customer;
import com.carrental.app.exception.ConflictException;
import com.carrental.app.exception.CustomerNotFoundException;
import com.carrental.app.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void create_newEmail_savesCustomer() {
        CustomerRequestDTO request = new CustomerRequestDTO("Jane", "Doe", "jane.doe@email.com", "514-555-9999");

        when(customerRepository.existsByEmail("jane.doe@email.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            customer.setId(5L);
            return customer;
        });

        CustomerResponseDTO response = customerService.create(request);

        assertEquals(5L, response.id());
        assertEquals("Jane", response.firstName());
        assertEquals("jane.doe@email.com", response.email());
    }

    @Test
    void create_duplicateEmail_throwsConflict() {
        CustomerRequestDTO request = new CustomerRequestDTO("Jane", "Doe", "john.smith@email.com", "514-555-9999");

        when(customerRepository.existsByEmail("john.smith@email.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> customerService.create(request));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void update_missingCustomer_throwsNotFound() {
        CustomerRequestDTO request = new CustomerRequestDTO("Jane", "Doe", "jane.doe@email.com", "514-555-9999");

        when(customerRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.update(42L, request));
    }

    @Test
    void delete_missingCustomer_throwsNotFound() {
        when(customerRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.delete(42L));
        verify(customerRepository, never()).delete(any());
    }
}
