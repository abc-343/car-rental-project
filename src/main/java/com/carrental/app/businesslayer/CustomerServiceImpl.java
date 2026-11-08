package com.carrental.app.businesslayer;

import com.carrental.app.domainlayer.Customer;
import com.carrental.app.dataaccesslayer.CustomerRepository;
import com.carrental.app.exception.BadRequestException;
import com.carrental.app.exception.ConflictException;
import com.carrental.app.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final String EMAIL_PATTERN = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerByCustomerId(String customerId) throws CustomerNotFoundException {
        return customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Override
    public Customer addCustomer(Customer customer) {
        validateCustomer(customer);

        String email = customer.getEmail();
        // Business rule: two customers cannot have the same email address.
        if (customerRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email " + email + " is already used");
        }

        customer.setId(null);                                  // null = new, the database generates the id
        customer.setCustomerId(UUID.randomUUID().toString());  // system: public id
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(String customerId, Customer updatedCustomer) throws CustomerNotFoundException {
        validateCustomer(updatedCustomer);
        Customer existingCustomer = getCustomerByCustomerId(customerId);

        String email = updatedCustomer.getEmail();
        // only another customer's email counts as a duplicate (keeping your own email is fine)
        if (customerRepository.existsByEmailIgnoreCaseAndCustomerIdNot(email, customerId)) {
            throw new ConflictException("Email " + email + " is already used");
        }

        // Preserve everything the client cannot change:
        // the ids, and the customer's rentals (otherwise saving an empty list would delete them).
        updatedCustomer.setId(existingCustomer.getId());
        updatedCustomer.setCustomerId(existingCustomer.getCustomerId());
        updatedCustomer.setRentals(existingCustomer.getRentals());

        return customerRepository.save(updatedCustomer);
    }

    @Override
    public void deleteByCustomerId(String customerId) throws CustomerNotFoundException {
        Customer customer = getCustomerByCustomerId(customerId);   // 404 if it doesn't exist
        // the customer's rentals are deleted too (cascade on Customer.rentals)
        customerRepository.delete(customer);
    }

    @Override
    public boolean existsByCustomerId(String customerId) {
        return customerRepository.existsByCustomerId(customerId);
    }

    // ---------------------------------------------------------------------
    // Validation and business rules
    // ---------------------------------------------------------------------

    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new BadRequestException("Customer cannot be null.");
        }

        if (customer.getFirstName() == null || customer.getFirstName().isBlank()) {
            throw new BadRequestException("First name is required.");
        }

        if (customer.getLastName() == null || customer.getLastName().isBlank()) {
            throw new BadRequestException("Last name is required.");
        }

        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new BadRequestException("Email is required.");
        }

        if (!customer.getEmail().matches(EMAIL_PATTERN)) {
            throw new BadRequestException("Email format is invalid.");
        }

        if (customer.getPhone() == null || customer.getPhone().isBlank()) {
            throw new BadRequestException("Phone is required.");
        }
    }
}
