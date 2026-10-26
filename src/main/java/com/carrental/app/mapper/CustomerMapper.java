package com.carrental.app.mapper;

import com.carrental.app.dto.request.CustomerRequestDTO;
import com.carrental.app.dto.response.CustomerResponseDTO;
import com.carrental.app.dto.response.CustomerSummaryDTO;
import com.carrental.app.dto.response.CustomerWithRentalsResponseDTO;
import com.carrental.app.dto.response.RentalResponseDTO;
import com.carrental.app.entity.Customer;

import java.util.List;

public class CustomerMapper {

    public static Customer toEntity(CustomerRequestDTO dto) {
        return new Customer(dto.firstName(), dto.lastName(), dto.email(), dto.phone());
    }

    // copies the request values onto an existing customer (used by PUT)
    public static void updateEntity(Customer customer, CustomerRequestDTO dto) {
        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
    }

    public static CustomerResponseDTO toResponse(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }

    public static CustomerSummaryDTO toSummary(Customer customer) {
        String fullName = customer.getFirstName() + " " + customer.getLastName();
        return new CustomerSummaryDTO(customer.getId(), fullName, customer.getEmail());
    }

    public static CustomerWithRentalsResponseDTO toWithRentals(Customer customer) {
        List<RentalResponseDTO> rentals = customer.getRentals().stream()
                .map(RentalMapper::toResponse)
                .toList();

        return new CustomerWithRentalsResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                rentals
        );
    }
}
