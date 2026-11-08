package com.carrental.app.dataaccesslayer;

import com.carrental.app.domainlayer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // customerId is the public id, e.g. CUS-0001 (the Long id stays internal)
    Optional<Customer> findByCustomerId(String customerId);

    boolean existsByEmailIgnoreCase(String email);

    // used on update: another customer (different customerId) already has this email
    boolean existsByEmailIgnoreCaseAndCustomerIdNot(String email, String customerId);
}
