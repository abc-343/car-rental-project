package com.carrental.app.service;

import com.carrental.app.dto.request.CustomerRequestDTO;
import com.carrental.app.dto.response.CustomerResponseDTO;
import com.carrental.app.dto.response.CustomerWithRentalsResponseDTO;
import com.carrental.app.entity.Customer;
import com.carrental.app.exception.ConflictException;
import com.carrental.app.exception.CustomerNotFoundException;
import com.carrental.app.mapper.CustomerMapper;
import com.carrental.app.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO findById(Long id) {
        return CustomerMapper.toResponse(getCustomerOrThrow(id));
    }

    @Transactional(readOnly = true)
    public CustomerWithRentalsResponseDTO findCustomerWithRentals(Long id) {
        return CustomerMapper.toWithRentals(getCustomerOrThrow(id));
    }

    @Transactional
    public CustomerResponseDTO create(CustomerRequestDTO request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email " + request.email() + " is already used");
        }
        Customer saved = customerRepository.save(CustomerMapper.toEntity(request));
        return CustomerMapper.toResponse(saved);
    }

    @Transactional
    public CustomerResponseDTO update(Long id, CustomerRequestDTO request) {
        Customer customer = getCustomerOrThrow(id);

        // only check the email if it changed, otherwise it would conflict with itself
        boolean emailChanged = !customer.getEmail().equalsIgnoreCase(request.email());
        if (emailChanged && customerRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email " + request.email() + " is already used");
        }

        CustomerMapper.updateEntity(customer, request);
        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = getCustomerOrThrow(id);
        customerRepository.delete(customer);
    }

    private Customer getCustomerOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
