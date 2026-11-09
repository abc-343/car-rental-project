package com.carrental.app.customermanagement.datamappinglayer;

import com.carrental.app.customermanagement.domainlayer.Customer;
import com.carrental.app.customermanagement.presentationlayer.dto.request.CustomerRequestDTO;
import com.carrental.app.customermanagement.presentationlayer.dto.response.CustomerResponseDTO;
import com.carrental.app.customermanagement.presentationlayer.dto.response.CustomerWithRentalsResponseDTO;
import com.carrental.app.rentalmanagement.datamappinglayer.RentalMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    private final RentalMapper rentalMapper;

    public CustomerMapper(RentalMapper rentalMapper) {
        this.rentalMapper = rentalMapper;
    }

    public CustomerResponseDTO entityToResponseModel(Customer customer) {
        return new CustomerResponseDTO(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }

    public List<CustomerResponseDTO> entityListToResponseModelList(List<Customer> customers) {
        return customers.stream()
                .map(this::entityToResponseModel)
                .toList();
    }

    // id / public id are not copied: the request never contains them (the server generates them)
    public Customer requestModelToEntity(CustomerRequestDTO request) {
        return new Customer(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhone());
    }

    // aggregated: customer + all of their rentals
    public CustomerWithRentalsResponseDTO entityToCustomerWithRentalsResponseModel(Customer customer) {
        return new CustomerWithRentalsResponseDTO(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                rentalMapper.entityListToResponseModelList(customer.getRentals())
        );
    }
}
