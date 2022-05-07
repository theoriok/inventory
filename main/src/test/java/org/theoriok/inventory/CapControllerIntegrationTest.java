package org.theoriok.inventory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.theoriok.inventory.persistence.entities.CapEntity;
import org.theoriok.inventory.persistence.entities.CountryEntity;
import org.theoriok.inventory.persistence.repositories.CapRepository;
import org.theoriok.inventory.persistence.repositories.CountryRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableJpaRepositories
class CapControllerIntegrationTest {
    @Autowired
    private CapRepository capRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        capRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void shouldReturnEmptyArrayWhenNoCapsFound() throws Exception {
        mvc.perform(get("/caps"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnCapWhenCapFound() throws Exception {
        var country = testCountry();
        countryRepository.save(country);
        capRepository.save(testCap(country));

        mvc.perform(get("/caps"))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJsonArray()));
    }

    @Language("JSON")
    private String expectedJsonArray() {
        return """
            [
                {
                    "business_id": "BE-1",
                    "name": "Belgian Cap",
                    "description": "This is a Belgian Cap",
                    "amount": 5,
                    "country": {
                        "name": "Belgium",
                        "code": "BE"
                    }
                }
            ]
            """;
    }

    @Test
    void shouldReturnNotFoundWhenCapNotFoundById() throws Exception {
        mvc.perform(get("/caps/BE-1"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(""));
    }

    @Test
    void shouldReturnCapWhenCapFoundById() throws Exception {
        var country = testCountry();
        countryRepository.save(country);
        capRepository.save(testCap(country));

        mvc.perform(get("/caps/BE-1"))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJsonObject()));
    }

    @Language("JSON")
    private String expectedJsonObject() {
        return """
            {
                "business_id": "BE-1",
                "name": "Belgian Cap",
                "description": "This is a Belgian Cap",
                "amount": 5,
                "country": {
                    "name": "Belgium",
                    "code": "BE"
                }
            }
            """;
    }

    private CapEntity testCap(CountryEntity country) {
        return new CapEntity("BE-1", "Belgian Cap", "This is a Belgian Cap", 5, country);
    }

    private CountryEntity testCountry() {
        return new CountryEntity("Belgium", "BE");
    }
}
