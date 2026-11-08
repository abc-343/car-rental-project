package com.carrental.app.presentationlayer.controller;

import com.carrental.app.businesslayer.CustomerService;
import com.carrental.app.domainlayer.Customer;
import com.carrental.app.presentationlayer.dto.request.CustomerRequestDTO;
import com.carrental.app.presentationlayer.dto.response.CustomerResponseDTO;
import com.carrental.app.presentationlayer.dto.response.CustomerWithRentalsResponseDTO;
import com.carrental.app.datamapping.CustomerMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerMapper.entityListToResponseModelList(customerService.getAllCustomers()));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable String customerId) {
        return ResponseEntity.ok(customerMapper.entityToResponseModel(customerService.getCustomerByCustomerId(customerId)));
    }

    // aggregated: customer + all their rentals
    @GetMapping("/{customerId}/rentals")
    public ResponseEntity<CustomerWithRentalsResponseDTO> getCustomerWithRentals(@PathVariable String customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        return ResponseEntity.ok(customerMapper.entityToCustomerWithRentalsResponseModel(customer));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO request) {
        Customer savedCustomer = customerService.addCustomer(customerMapper.requestModelToEntity(request));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{customerId}")
                .buildAndExpand(savedCustomer.getCustomerId())
                .toUri();

        return ResponseEntity.created(location).body(customerMapper.entityToResponseModel(savedCustomer));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable String customerId,
                                                              @Valid @RequestBody CustomerRequestDTO request) {
        Customer updatedCustomer = customerService.updateCustomer(customerId, customerMapper.requestModelToEntity(request));
        return ResponseEntity.ok(customerMapper.entityToResponseModel(updatedCustomer));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteByCustomerId(customerId);
        return ResponseEntity.noContent().build();
    }
}
