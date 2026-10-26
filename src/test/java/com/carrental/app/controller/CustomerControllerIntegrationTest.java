package com.carrental.app.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllCustomers_returns200() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(10)));
    }

    @Test
    void getCustomerById_existingCustomer_returns200() throws Exception {
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.smith@email.com"));
    }

    @Test
    void getCustomerById_missingCustomer_returns404() throws Exception {
        mockMvc.perform(get("/api/customers/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer 9999 not found"));
    }

    @Test
    void getCustomerWithRentals_returnsCustomerAndRentals() throws Exception {
        mockMvc.perform(get("/api/customers/1/rentals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.rentals.length()").value(2))
                .andExpect(jsonPath("$.rentals[0].customerId").value(1));
    }

    @Test
    void getCustomerWithRentals_missingCustomer_returns404() throws Exception {
        mockMvc.perform(get("/api/customers/9999/rentals"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer_validBody_returns201WithLocation() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson("create.test@email.com")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/customers/")))
                .andExpect(jsonPath("$.email").value("create.test@email.com"));
    }

    @Test
    void createCustomer_invalidEmail_returns400() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson("not-an-email")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void createCustomer_duplicateEmail_returns409() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson("john.smith@email.com")))
                .andExpect(status().isConflict());
    }

    @Test
    void updateCustomer_existingCustomer_returns200() throws Exception {
        Long id = createCustomer("update.test@email.com");
        String updated = """
                {
                  "firstName": "Updated",
                  "lastName": "Person",
                  "email": "update.test@email.com",
                  "phone": "514-555-0000"
                }
                """;

        mockMvc.perform(put("/api/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void deleteCustomer_existingCustomer_returns204() throws Exception {
        Long id = createCustomer("delete.test@email.com");

        mockMvc.perform(delete("/api/customers/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/customers/" + id))
                .andExpect(status().isNotFound());
    }

    private String customerJson(String email) {
        return """
                {
                  "firstName": "Test",
                  "lastName": "Person",
                  "email": "%s",
                  "phone": "514-555-1234"
                }
                """.formatted(email);
    }

    private Long createCustomer(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson(email)))
                .andExpect(status().isCreated())
                .andReturn();

        Number id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }
}
