package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.theoriok.inventory.persistence.entities.CapEntity;
import org.theoriok.inventory.persistence.entities.CountryEntity;
import org.theoriok.inventory.persistence.repositories.CapRepository;
import org.theoriok.inventory.persistence.repositories.CountryRepository;

import java.util.stream.Stream;

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

        @ParameterizedTest
        @MethodSource("invalidCapData")
        void shouldValidateFields(String invalidCapJson, String expectedProblemJson) throws Exception {
            mvc.perform(put("/caps")
                    .contentType(APPLICATION_JSON)
                    .content(invalidCapJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedProblemJson));
        }

        private static Stream<Arguments> invalidCapData() {
            return Stream.of(
                arguments(capToUpsertWithNulls(), expectedBlankValidationProblemJson()),
                arguments(capToUpsertWithBlankStrings(), expectedBlankValidationProblemJson()),
                arguments(capToUpsertWithWhitespace(), expectedBlankValidationProblemJson()),
                arguments(capToUpsertWithFieldsTooLong(), expectedMaxLengthValidationProblemJson()),
                arguments(capToUpsertWithNegativeAmount(), expectedPositiveAmountValidationProblemJson())
            );
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
        private static String capToUpsertWithNulls() {
            return """
                {
                
                }
                """;
        }

        @Language("JSON")
        private static String capToUpsertWithBlankStrings() {
            return """
                {
                    "business_id": "",
                    "name": "",
                    "description": "",
                    "amount": 1,
                    "country": ""
                }
                """;
        }

        @Language("JSON")
        private static String capToUpsertWithWhitespace() {
            return """
                {
                    "business_id": "   ",
                    "name": "   ",
                    "description": "   ",
                    "amount": 1,
                    "country": "   "
                }
                """;
        }

        @Language("JSON")
        private static String capToUpsertWithFieldsTooLong() {
            return """
                {
                    "business_id": "%s",
                    "name": "%s",
                    "description": "%s",
                    "amount": 1,
                    "country": "%s"
                }
                """.formatted(
                "A".repeat(256),
                "B".repeat(256),
                "C".repeat(5001),
                "D".repeat(256)
            );
        }

        @Language("JSON")
        private static String capToUpsertWithNegativeAmount() {
            return """
                {
                    "business_id": "BE-1",
                    "name": "Belgian Cap",
                    "description": "This is a Belgian Cap",
                    "amount": -1,
                    "country": "BE"
                }
                """;
        }

        @Language("JSON")
        private static String expectedBlankValidationProblemJson() {
            return """
                {
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Validation failed",
                  "instance": "/caps",
                  "errors": {
                    "businessId": "must not be blank",
                    "name": "must not be blank",
                    "description": "must not be blank",
                    "country": "must not be blank"
                  }
                }
                """;
        }

        @Language("JSON")
        private static String expectedMaxLengthValidationProblemJson() {
            return """
                {
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Validation failed",
                  "instance": "/caps",
                  "errors": {
                    "businessId": "size must be between 0 and 255",
                    "name": "size must be between 0 and 255",
                    "description": "size must be between 0 and 5000",
                    "country": "size must be between 0 and 10"
                  }
                }
                """;
        }

        @Language("JSON")
        private static String expectedPositiveAmountValidationProblemJson() {
            return """
                {
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Validation failed",
                  "instance": "/caps",
                  "errors": {
                    "amount": "must be greater than 0"
                  }
                }
                """;
        }

        @Language("JSON")
        private static String expectedBadRequestProblemJson() {
            return """
                {
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Unknown country BE",
                  "instance": "/caps"
                }
                """;
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
    private String expectedCapNotFoundProblemJson() {
        return """
            {
              "title": "Not Found",
              "status": 404,
              "instance": "/caps/BE-1"
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
