package org.theoriok.inventory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.theoriok.inventory.persistence.entities.CountryEntity;
import org.theoriok.inventory.persistence.repositories.CountryRepository;

public class CountryIntegrationTest extends IntegrationTest {

    @Autowired
    private CountryRepository countryRepository;

    @Nested
    class Find {
        @Test
        void shouldReturnEmptyArrayWhenNoCountriesFound() throws Exception {
            mvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        }

        @Test
        void shouldReturnCountryWhenCountryFound() throws Exception {
            var country = testCountry();
            countryRepository.save(country);

            mvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonArray()));
        }

        @Test
        void shouldReturnNotFoundWhenCountryNotFoundById() throws Exception {
            mvc.perform(get("/countries/BE-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        }

        @Test
        void shouldReturnCountryWhenCountryFoundById() throws Exception {
            var country = testCountry("BE");
            countryRepository.save(country);

            mvc.perform(get("/countries/BE"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonObject()));
        }
    }

    @Language("JSON")
    private String expectedJsonArray() {
        return """
            [
                {
                    "name": "Belgium",
                    "code": "BE"
                }
            ]
            """;
    }

    @Language("JSON")
    private String expectedJsonObject() {
        return """
            {
                "name": "Belgium",
                "code": "BE"
            }
            """;
    }

    private CountryEntity testCountry() {
        return testCountry("BE");
    }

    private CountryEntity testCountry(String code) {
        return new CountryEntity("Belgium", code);
    }
}
