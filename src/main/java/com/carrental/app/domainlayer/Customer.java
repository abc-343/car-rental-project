package com.carrental.app.domainlayer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Lombok generates the getters, setters and the no-argument constructor JPA needs.
// The warning suppressed below comes from IntelliJ having no database connection to check the
// table/column names against (H2 only exists while the app runs); the tables are created from these annotations.
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // internal database id, never sent to the client

    // public id used in the API and in URLs, e.g. CUS-0001
    @Column(name = "customer_id", nullable = false, unique = true, length = 20)
    private String customerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    // one customer -> many rentals. If a customer is deleted their rentals are deleted too
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals = new ArrayList<>();

    public Customer(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}
