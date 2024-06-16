package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.theoriok.inventory.persistence.entities.CapEntity;
import org.theoriok.inventory.persistence.entities.CountryEntity;
import org.theoriok.inventory.persistence.repositories.CapRepository;
import org.theoriok.inventory.persistence.repositories.CountryRepository;

class CapIntegrationTest extends IntegrationTest {

    @Autowired
    private CapRepository capRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Nested
    class Find {
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
                    .andExpect(content().json(expectedCaps()));
        }

        @Test
        void shouldReturnCapsByCountry() throws Exception {
            var country = testCountry("BE");
            countryRepository.save(country);
            capRepository.save(testCap(country, "BE-1"));
            var differentCountry = testCountry("NL");
            countryRepository.save(differentCountry);
            capRepository.save(testCap(differentCountry, "NL-2"));

            mvc.perform(get("/caps?country=BE"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedCaps()));
        }

        @Test
        void shouldReturnEmptyArrayWhenNoCapsFoundForCountry() throws Exception {
            var country = testCountry("BE");
            countryRepository.save(country);
            capRepository.save(testCap(country, "BE-1"));
            var differentCountry = testCountry("NL");
            countryRepository.save(differentCountry);
            capRepository.save(testCap(differentCountry, "NL-2"));

            mvc.perform(get("/caps?country=US"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }

        @Test
        void shouldReturnNotFoundWhenCapNotFoundById() throws Exception {
            mvc.perform(get("/caps/BE-1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expectedCapNotFoundProblemJson()));
        }

        @Test
        void shouldReturnCapWhenCapFoundById() throws Exception {
            var country = testCountry();
            countryRepository.save(country);
            capRepository.save(testCap(country));

            mvc.perform(get("/caps/BE-1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedCap()));
        }
    }

    @Nested
    class Upsert {
        @Test
        void shouldInsertNewCap() throws Exception {
            var country = testCountry();
            countryRepository.save(country);

            mvc.perform(put("/caps")
                            .contentType(APPLICATION_JSON)
                            .content(capToUpsert()))
                    .andExpect(status().isNoContent());

            assertThat(capRepository.findAll())
                    .singleElement()
                    .returns("BE-1", from(CapEntity::getBusinessId))
                    .returns("Belgian Cap", from(CapEntity::getName))
                    .returns("This is a Belgian Cap", from(CapEntity::getDescription))
                    .returns(5, from(CapEntity::getAmount))
                    .returns(country, from(CapEntity::getCountry));
        }

        @Test
        void shouldUpdateExistingCap() throws Exception {
            var country = testCountry();
            countryRepository.save(country);
            capRepository.save(testCapWithSameIdButDifferentValues(country));

            mvc.perform(put("/caps")
                            .contentType(APPLICATION_JSON)
                            .content(capToUpsert()))
                    .andExpect(status().isNoContent());

            assertThat(capRepository.findAll())
                    .singleElement()
                    .returns("BE-1", from(CapEntity::getBusinessId))
                    .returns("Belgian Cap", from(CapEntity::getName))
                    .returns("This is a Belgian Cap", from(CapEntity::getDescription))
                    .returns(5, from(CapEntity::getAmount))
                    .returns(country, from(CapEntity::getCountry));
        }

        @Test
        void shouldHandleUnknownCountry() throws Exception {
            mvc.perform(put("/caps")
                            .contentType(APPLICATION_JSON)
                            .content(capToUpsert()))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(expectedBadRequestProblemJson()));
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldDeleteCapWhenCapFoundById() throws Exception {
            var country = testCountry();
            countryRepository.save(country);
            capRepository.save(testCap(country));

            mvc.perform(delete("/caps/BE-1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
            assertThat(capRepository.findAll()).isEmpty();
        }

        @Test
        void shouldReturnNotFoundWhenCapNotFoundByIdForDelete() throws Exception {
            mvc.perform(delete("/caps/BE-1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expectedCapNotFoundProblemJson()));
        }
    }

    @Language("JSON")
    private String capToUpsert() {
        return """
                {
                    "business_id": "BE-1",
                    "name": "Belgian Cap",
                    "description": "This is a Belgian Cap",
                    "amount": 5,
                    "country": "BE"
                }
                """;
    }

    @Language("JSON")
    private String expectedCaps() {
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

    @Language("JSON")
    private String expectedCap() {
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

    @Language("JSON")
    private String expectedCapNotFoundProblemJson() {
        return """
                {
                  "type": "about:blank",
                  "title": "Not Found",
                  "status": 404,
                  "instance": "/caps/BE-1"
                }
                """;
    }

    @Language("JSON")
    private static String expectedBadRequestProblemJson() {
        return """
                {
                  "type": "about:blank",
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Unknown country BE",
                  "instance": "/caps"
                }
                """;
    }

    private CapEntity testCap(CountryEntity country) {
        return testCap(country, "BE-1");
    }

    private CapEntity testCap(CountryEntity country, String businessId) {
        return new CapEntity(businessId, "Belgian Cap", "This is a Belgian Cap", 5, country);
    }

    private CapEntity testCapWithSameIdButDifferentValues(CountryEntity country) {
        return new CapEntity("BE-1", "Jupiler", "Stella Artois en al", 1, country);
    }

    private CountryEntity testCountry() {
        return testCountry("BE");
    }

    private CountryEntity testCountry(String code) {
        return new CountryEntity("Belgium", code);
    }
}
