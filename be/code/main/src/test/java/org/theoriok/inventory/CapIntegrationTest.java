package org.theoriok.inventory;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import java.util.UUID;
import java.util.stream.Stream;

class CapIntegrationTest extends IntegrationTest {

    @Autowired
    private CapRepository capRepository;

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
            jdbcAggregateTemplate.insert(country);
            var cap = jdbcAggregateTemplate.insert(testCap(country));

            mvc.perform(get("/caps"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedCaps(CapId.from(cap.id()))));
        }

        @Test
        void shouldReturnCapsByCountry() throws Exception {
            var country = testCountry("BE");
            jdbcAggregateTemplate.insert(country);
            var cap = jdbcAggregateTemplate.insert(testCap(country));
            var differentCountry = testCountry("NL");
            jdbcAggregateTemplate.insert(differentCountry);
            jdbcAggregateTemplate.insert(testCap(differentCountry));

            mvc.perform(get("/caps?country=BE"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedCaps(CapId.from(cap.id()))));
        }

        @Test
        void shouldReturnEmptyArrayWhenNoCapsFoundForCountry() throws Exception {
            var country = testCountry("BE");
            jdbcAggregateTemplate.insert(country);
            jdbcAggregateTemplate.insert(testCap(country));
            var differentCountry = testCountry("NL");
            jdbcAggregateTemplate.insert(differentCountry);
            jdbcAggregateTemplate.insert(testCap(differentCountry));

            mvc.perform(get("/caps?country=US"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        }

        @Test
        void shouldReturnNotFoundWhenCapNotFoundById() throws Exception {
            var randomId = CapId.randomCapId();
            mvc.perform(get("/caps/" + randomId.value()))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedCapNotFoundProblemJson(randomId)));
        }

        @Test
        void shouldReturnCapWhenCapFoundById() throws Exception {
            var country = testCountry();
            jdbcAggregateTemplate.insert(country);
            var cap = jdbcAggregateTemplate.insert(testCap(country));

            mvc.perform(get("/caps/" + cap.id()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedCap(CapId.from(cap.id()))));
        }

        @Language("JSON")
        private String expectedCaps(CapId id) {
            return """
                [
                    {
                        "id": "%s",
                        "name": "Belgian Cap",
                        "description": "This is a Belgian Cap",
                        "amount": 5,
                        "country": {
                            "name": "Belgium",
                            "code": "BE"
                        }
                    }
                ]
                """.formatted(id.value());
        }

        @Language("JSON")
        private String expectedCap(CapId id) {
            return """
                {
                    "id": "%s",
                    "name": "Belgian Cap",
                    "description": "This is a Belgian Cap",
                    "amount": 5,
                    "country": {
                        "name": "Belgium",
                        "code": "BE"
                    }
                }
                """.formatted(id.value());
        }
    }

    @Nested
    class Create {
        @Test
        void shouldCreateNewCap() throws Exception {
            var country = testCountry();
            jdbcAggregateTemplate.insert(country);

            var result = mvc.perform(post("/caps")
                    .contentType(APPLICATION_JSON)
                    .content(capToCreate()))
                .andExpect(status().isCreated())
                .andReturn();

            assertThatJson(result.getResponse().getContentAsString())
                .isEqualTo("""
                    {
                        "id": "${json-unit.any-string}",
                        "name": "Belgian Cap",
                        "description": "This is a Belgian Cap",
                        "amount": 5,
                        "country": {
                            "name": "Belgium",
                            "code": "BE"
                        }
                    }
                    """);

            assertThat(capRepository.findAll())
                .singleElement()
                .returns("Belgian Cap", from(CapEntity::name))
                .returns("This is a Belgian Cap", from(CapEntity::description))
                .returns(5, from(CapEntity::amount))
                .returns(country.code(), from(CapEntity::countryCode));
        }

        @Test
        void shouldHandleUnknownCountry() throws Exception {
            mvc.perform(post("/caps")
                    .contentType(APPLICATION_JSON)
                    .content(capToCreate()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedBadRequestProblemJson()));
        }

        @ParameterizedTest
        @MethodSource("invalidCapData")
        void shouldValidateFields(String invalidCapJson, String expectedProblemJson) throws Exception {
            mvc.perform(post("/caps")
                    .contentType(APPLICATION_JSON)
                    .content(invalidCapJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedProblemJson));
        }

        private static Stream<Arguments> invalidCapData() {
            return Stream.of(
                arguments(capToCreateWithNulls(), expectedBlankValidationProblemJson()),
                arguments(capToCreateWithBlankStrings(), expectedBlankValidationProblemJson()),
                arguments(capToCreateWithWhitespace(), expectedBlankValidationProblemJson()),
                arguments(capToCreateWithFieldsTooLong(), expectedMaxLengthValidationProblemJson()),
                arguments(capToCreateWithNegativeAmount(), expectedPositiveAmountValidationProblemJson())
            );
        }

        @Language("JSON")
        private String capToCreate() {
            return """
                {
                    "name": "Belgian Cap",
                    "description": "This is a Belgian Cap",
                    "amount": 5,
                    "country": "BE"
                }
                """;
        }

        @Language("JSON")
        private static String capToCreateWithNulls() {
            return """
                {
                
                }
                """;
        }

        @Language("JSON")
        private static String capToCreateWithBlankStrings() {
            return """
                {
                    "name": "",
                    "description": "",
                    "amount": 1,
                    "country": ""
                }
                """;
        }

        @Language("JSON")
        private static String capToCreateWithWhitespace() {
            return """
                {
                    "name": "   ",
                    "description": "   ",
                    "amount": 1,
                    "country": "   "
                }
                """;
        }

        @Language("JSON")
        private static String capToCreateWithFieldsTooLong() {
            return """
                {
                    "name": "%s",
                    "description": "%s",
                    "amount": 1,
                    "country": "%s"
                }
                """.formatted(
                "B".repeat(256),
                "C".repeat(5001),
                "D".repeat(256)
            );
        }

        @Language("JSON")
        private static String capToCreateWithNegativeAmount() {
            return """
                {
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
    class Update {
        @Test
        void shouldUpdateExistingCap() throws Exception {
            var country = testCountry();
            jdbcAggregateTemplate.insert(country);
            var cap = jdbcAggregateTemplate.insert(testCap(country));

            mvc.perform(put("/caps/" + cap.id())
                    .contentType(APPLICATION_JSON)
                    .content(capToUpdate()))
                .andExpect(status().isNoContent());

            assertThat(capRepository.findAll())
                .singleElement()
                .returns(cap.id(), from(CapEntity::id))
                .returns("Updated Cap", from(CapEntity::name))
                .returns("Updated description", from(CapEntity::description))
                .returns(10, from(CapEntity::amount))
                .returns(country.code(), from(CapEntity::countryCode));
        }

        @Test
        void shouldReturnNotFoundWhenUpdatingNonExistentCap() throws Exception {
            var randomId = CapId.randomCapId();
            mvc.perform(put("/caps/" + randomId.value())
                    .contentType(APPLICATION_JSON)
                    .content(capToUpdate()))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedCapNotFoundProblemJson(randomId)));
        }

        @Test
        void shouldHandleUnknownCountry() throws Exception {
            var country = testCountry();
            jdbcAggregateTemplate.insert(country);
            var cap = jdbcAggregateTemplate.insert(testCap(country));

            mvc.perform(put("/caps/" + cap.id())
                    .contentType(APPLICATION_JSON)
                    .content(capToUpdateWithUnknownCountry()))
                .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("invalidCapData")
        void shouldValidateFields(String invalidCapJson, String expectedProblemJson) throws Exception {
            var country = testCountry();
            jdbcAggregateTemplate.insert(country);
            var cap = jdbcAggregateTemplate.insert(testCap(country));
            mvc.perform(put("/caps/" + cap.id())
                    .contentType(APPLICATION_JSON)
                    .content(invalidCapJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedProblemJson.formatted(cap.id())));
        }

        private static Stream<Arguments> invalidCapData() {
            return Stream.of(
                arguments(capToUpdateWithNulls(), expectedBlankValidationProblemJson()),
                arguments(capToUpdateWithBlankStrings(), expectedBlankValidationProblemJson()),
                arguments(capToUpdateWithWhitespace(), expectedBlankValidationProblemJson()),
                arguments(capToUpdateWithFieldsTooLong(), expectedMaxLengthValidationProblemJson()),
                arguments(capToUpdateWithNegativeAmount(), expectedPositiveAmountValidationProblemJson())
            );
        }

        @Language("JSON")
        private String capToUpdate() {
            return """
                {
                    "name": "Updated Cap",
                    "description": "Updated description",
                    "amount": 10,
                    "country": "BE"
                }
                """;
        }

        @Language("JSON")
        private String capToUpdateWithUnknownCountry() {
            return """
                {
                    "name": "Updated Cap",
                    "description": "Updated description",
                    "amount": 10,
                    "country": "XX"
                }
                """;
        }

        @Language("JSON")
        private static String capToUpdateWithNulls() {
            return """
                {
                
                }
                """;
        }

        @Language("JSON")
        private static String capToUpdateWithBlankStrings() {
            return """
                {
                    "name": "",
                    "description": "",
                    "amount": 1,
                    "country": ""
                }
                """;
        }

        @Language("JSON")
        private static String capToUpdateWithWhitespace() {
            return """
                {
                    "name": "   ",
                    "description": "   ",
                    "amount": 1,
                    "country": "   "
                }
                """;
        }

        @Language("JSON")
        private static String capToUpdateWithFieldsTooLong() {
            return """
                {
                    "name": "%s",
                    "description": "%s",
                    "amount": 1,
                    "country": "%s"
                }
                """.formatted(
                "B".repeat(256),
                "C".repeat(5001),
                "D".repeat(256)
            );
        }

        @Language("JSON")
        private static String capToUpdateWithNegativeAmount() {
            return """
                {
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
                  "instance": "/caps/%s",
                  "errors": {
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
                  "instance": "/caps/%s",
                  "errors": {
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
                  "instance": "/caps/%s",
                  "errors": {
                    "amount": "must be greater than 0"
                  }
                }
                """;
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldDeleteCapWhenCapFoundById() throws Exception {
            var country = testCountry();
            jdbcAggregateTemplate.insert(country);
            var cap = jdbcAggregateTemplate.insert(testCap(country));

            mvc.perform(delete("/caps/" + cap.id()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
            assertThat(capRepository.findAll()).isEmpty();
        }

        @Test
        void shouldReturnNotFoundWhenCapNotFoundByIdForDelete() throws Exception {
            var randomId = CapId.randomCapId();
            mvc.perform(delete("/caps/" + randomId.value()))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedCapNotFoundProblemJson(randomId)));
        }
    }

    @Language("JSON")
    private String expectedCapNotFoundProblemJson(CapId id) {
        return """
            {
              "title": "Not Found",
              "status": 404,
              "instance": "/caps/%s"
            }
            """.formatted(id.value());
    }

    private CapEntity testCap(CountryEntity country) {
        return new CapEntity(CapId.randomCapId().toUuid(), "Belgian Cap", "This is a Belgian Cap", 5, country.code());
    }

    private CountryEntity testCountry() {
        return testCountry("BE");
    }

    private CountryEntity testCountry(String code) {
        return new CountryEntity(UUID.randomUUID(), "Belgium", code);
    }
}
