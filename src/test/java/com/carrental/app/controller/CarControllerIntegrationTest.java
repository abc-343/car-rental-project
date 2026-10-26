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

// integration tests: the whole app is started with the H2 database and data.sql
@SpringBootTest
@AutoConfigureMockMvc
class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_CAR = """
            {
              "make": "Toyota",
              "model": "Camry",
              "year": 2025,
              "dailyRate": 65.00,
              "status": "AVAILABLE"
            }
            """;

    @Test
    void getAllCars_returns200AndSeededCars() throws Exception {
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(10)));
    }

    @Test
    void getCarById_existingCar_returns200() throws Exception {
        mockMvc.perform(get("/api/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.make").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"));
    }

    @Test
    void getCarById_missingCar_returns404WithErrorBody() throws Exception {
        mockMvc.perform(get("/api/cars/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Car 9999 not found"))
                .andExpect(jsonPath("$.path").value("/api/cars/9999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getCarById_badId_returns400() throws Exception {
        mockMvc.perform(get("/api/cars/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCarWithRentals_returnsCarAndItsRentals() throws Exception {
        mockMvc.perform(get("/api/cars/1/rentals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Toyota"))
                .andExpect(jsonPath("$.rentals.length()").value(2))
                .andExpect(jsonPath("$.rentals[0].carId").value(1));
    }

    @Test
    void createCar_validBody_returns201WithLocation() throws Exception {
        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CAR))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/cars/")))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.model").value("Camry"));
    }

    @Test
    void createCar_invalidBody_returns400WithFieldErrors() throws Exception {
        String invalidCar = """
                {
                  "make": "",
                  "model": "Camry",
                  "year": 1500,
                  "dailyRate": -10,
                  "status": "AVAILABLE"
                }
                """;

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCar))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.make").exists())
                .andExpect(jsonPath("$.fieldErrors.year").exists())
                .andExpect(jsonPath("$.fieldErrors.dailyRate").exists());
    }

    @Test
    void createCar_badStatusValue_returns400() throws Exception {
        String badStatus = VALID_CAR.replace("AVAILABLE", "FLYING");

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badStatus))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCar_existingCar_returns200AndIsIdempotent() throws Exception {
        Long id = createCar();
        String updated = VALID_CAR.replace("Camry", "Camry Hybrid");

        // calling PUT twice gives the same result
        for (int i = 0; i < 2; i++) {
            mockMvc.perform(put("/api/cars/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updated))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.model").value("Camry Hybrid"));
        }
    }

    @Test
    void updateCar_missingCar_returns404() throws Exception {
        mockMvc.perform(put("/api/cars/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CAR))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCar_existingCar_returns204ThenGetReturns404() throws Exception {
        Long id = createCar();

        mockMvc.perform(delete("/api/cars/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/cars/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCar_missingCar_returns404() throws Exception {
        mockMvc.perform(delete("/api/cars/9999"))
                .andExpect(status().isNotFound());
    }

    // helper: creates a new car and returns its id
    private Long createCar() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CAR))
                .andExpect(status().isCreated())
                .andReturn();

        Number id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }
}
