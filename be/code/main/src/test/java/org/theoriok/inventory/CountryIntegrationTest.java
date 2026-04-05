package org.theoriok.inventory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.theoriok.inventory.persistence.entities.CountryEntity;

import java.util.UUID;

class CountryIntegrationTest extends IntegrationTest {

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
            jdbcAggregateTemplate.insert(country);

            mvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedCountries()));
        }

        @Test
        void shouldReturnNotFoundWhenCountryNotFoundById() throws Exception {
            mvc.perform(get("/countries/BE-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedCountryNotFoundProblemJson()));
        }

        @Test
        void shouldReturnCountryWhenCountryFoundById() throws Exception {
            var country = testCountry("BE");
            jdbcAggregateTemplate.insert(country);

            mvc.perform(get("/countries/BE"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedCountry()));
        }
    }

    @Language("JSON")
    private String expectedCountries() {
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
    private String expectedCountry() {
        return """
            {
                "name": "Belgium",
                "code": "BE"
            }
            """;
    }

    @Language("JSON")
    private String expectedCountryNotFoundProblemJson() {
        return """
                {
                  "title": "Not Found",
                  "status": 404,
                  "instance": "/countries/BE-1"
                }
                """;
    }

    private CountryEntity testCountry() {
        return testCountry("BE");
    }

    @SuppressWarnings("SameParameterValue")
    private CountryEntity testCountry(String code) {
        return new CountryEntity(UUID.randomUUID(), "Belgium", code);
    }
}
