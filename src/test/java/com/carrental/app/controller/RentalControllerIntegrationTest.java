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
class RentalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllRentals_returns200() throws Exception {
        mockMvc.perform(get("/api/rentals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(10)));
    }

    @Test
    void getRentalById_existingRental_returns200() throws Exception {
        mockMvc.perform(get("/api/rentals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.carId").value(1))
                .andExpect(jsonPath("$.totalCost").value(239.96));
    }

    @Test
    void getRentalById_missingRental_returns404() throws Exception {
        mockMvc.perform(get("/api/rentals/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Rental 9999 not found"));
    }

    @Test
    void getRentalWithDetails_returnsCustomerAndCarSummary() throws Exception {
        mockMvc.perform(get("/api/rentals/1/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.fullName").value("John Smith"))
                .andExpect(jsonPath("$.car.make").value("Toyota"));
    }

    @Test
    void createRental_validBody_returns201AndCalculatesCost() throws Exception {
        mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rentalJson(2, 5, "2026-12-01", "2026-12-05")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/rentals/")))
                // car 5 costs 52.00 per day, 4 days = 208.00
                .andExpect(jsonPath("$.totalCost").value(208.00))
                .andExpect(jsonPath("$.status").value("RESERVED"));
    }

    @Test
    void createRental_missingFields_returns400() throws Exception {
        mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.customerId").exists())
                .andExpect(jsonPath("$.fieldErrors.carId").exists())
                .andExpect(jsonPath("$.fieldErrors.startDate").exists());
    }

    @Test
    void createRental_endBeforeStart_returns400() throws Exception {
        mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rentalJson(2, 5, "2026-12-05", "2026-12-01")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("endDate must not be before startDate"));
    }

    @Test
    void createRental_unknownCustomer_returns404() throws Exception {
        mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rentalJson(9999, 5, "2026-12-01", "2026-12-05")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer 9999 not found"));
    }

    @Test
    void updateRental_existingRental_returns200AndRecalculatesCost() throws Exception {
        Long id = createRental();

        // car 3 costs 74.99 per day, 2 days = 149.98
        mockMvc.perform(put("/api/rentals/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rentalJson(2, 3, "2026-12-01", "2026-12-03")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(3))
                .andExpect(jsonPath("$.totalCost").value(149.98));
    }

    @Test
    void deleteRental_existingRental_returns204() throws Exception {
        Long id = createRental();

        mockMvc.perform(delete("/api/rentals/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/rentals/" + id))
                .andExpect(status().isNotFound());
    }

    private String rentalJson(long customerId, long carId, String start, String end) {
        return """
                {
                  "customerId": %d,
                  "carId": %d,
                  "startDate": "%s",
                  "endDate": "%s",
                  "status": "RESERVED"
                }
                """.formatted(customerId, carId, start, end);
    }

    private Long createRental() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rentalJson(2, 5, "2026-12-01", "2026-12-05")))
                .andExpect(status().isCreated())
                .andReturn();

        Number id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }
}
