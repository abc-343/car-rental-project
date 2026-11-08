package com.carrental.app.businesslayer;

import com.carrental.app.domainlayer.Customer;
import com.carrental.app.exception.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();

    Customer getCustomerByCustomerId(String customerId) throws CustomerNotFoundException;

    Customer addCustomer(Customer customer);

    Customer updateCustomer(String customerId, Customer updatedCustomer) throws CustomerNotFoundException;

    void deleteByCustomerId(String customerId) throws CustomerNotFoundException;
}
