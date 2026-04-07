package org.theoriok.inventory;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
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
import org.theoriok.inventory.persistence.entities.BookEntity;
import org.theoriok.inventory.persistence.repositories.BookRepository;

import java.util.stream.Stream;

class BookIntegrationTest extends IntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Nested
    class Find {
        @Test
        void shouldReturnEmptyArrayWhenNoBookFound() throws Exception {
            mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        }

        @Test
        void shouldReturnBookWhenBookFound() throws Exception {
            var book = jdbcAggregateTemplate.insert(testBook());

            mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBooks(BookId.from(book.id()))));
        }

        @Test
        void shouldReturnBookWhenBookFoundById() throws Exception {
            var book = jdbcAggregateTemplate.insert(testBook());

            mvc.perform(get("/books/" + book.id()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBook(BookId.from(book.id()))));
        }

        @Test
        void shouldReturnNotFoundWhenBookNotFoundById() throws Exception {
            var randomId = BookId.randomBookId();
            mvc.perform(get("/books/" + randomId.value()))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedBookNotFoundProblemJson(randomId)));
        }

        @Test
        void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
            mvc.perform(get("/books/not-a-uuid"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(content().json("""
                    {
                      "title": "Internal Server Error",
                      "status": 500,
                      "detail": "Something went wrong"
                    }
                    """));
        }

        @Language("JSON")
        private String expectedBooks(BookId id) {
            return """
                [
                    {
                        "id": "%s",
                        "title": "The Hobbit",
                        "author": "JRR Tolkien",
                        "description": "In a hole under the ground, there lived a Hobbit."
                    }
                ]
                """.formatted(id.value());
        }

        @Language("JSON")
        private String expectedBook(BookId id) {
            return """
                {
                    "id": "%s",
                    "title": "The Hobbit",
                    "author": "JRR Tolkien",
                    "description": "In a hole under the ground, there lived a Hobbit."
                }
                """.formatted(id.value());
        }
    }

    @Nested
    class Create {
        @Test
        void shouldCreateNewBook() throws Exception {
            var result = mvc.perform(post("/books")
                    .contentType(APPLICATION_JSON)
                    .content(bookToCreate()))
                .andExpect(status().isCreated())
                .andReturn();

            assertThatJson(result.getResponse().getContentAsString())
                .isEqualTo("""
                    {
                        "id": "${json-unit.any-string}",
                        "title": "The Hobbit",
                        "author": "JRR Tolkien",
                        "description": "In a hole under the ground, there lived a Hobbit."
                    }
                    """);

            assertThat(bookRepository.findAll())
                .singleElement()
                .returns("The Hobbit", from(BookEntity::title))
                .returns("JRR Tolkien", from(BookEntity::author))
                .returns("In a hole under the ground, there lived a Hobbit.", from(BookEntity::description));
        }

        @ParameterizedTest
        @MethodSource("invalidBookData")
        void shouldValidateFields(String invalidBookJson, String expectedProblemJson) throws Exception {
            mvc.perform(post("/books")
                    .contentType(APPLICATION_JSON)
                    .content(invalidBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedProblemJson));
        }

        private static Stream<Arguments> invalidBookData() {
            return Stream.of(
                arguments(bookToCreateWithNulls(), expectedBlankValidationProblemJson()),
                arguments(bookToCreateWithBlankStrings(), expectedBlankValidationProblemJson()),
                arguments(bookToCreateWithWhitespace(), expectedBlankValidationProblemJson()),
                arguments(bookToCreateWithFieldsTooLong(), expectedMaxLengthValidationProblemJson())
            );
        }

        @Language("JSON")
        private static String bookToCreateWithNulls() {
            return """
                {
                
                }
                """;
        }

        @Language("JSON")
        private static String bookToCreateWithBlankStrings() {
            return """
                {
                    "title": "",
                    "author": "",
                    "description": ""
                }
                """;
        }

        @Language("JSON")
        private static String bookToCreateWithWhitespace() {
            return """
                {
                    "title": "   ",
                    "author": "   ",
                    "description": "   "
                }
                """;
        }

        @Language("JSON")
        private String bookToCreate() {
            return """
                {
                    "title": "The Hobbit",
                    "author": "JRR Tolkien",
                    "description": "In a hole under the ground, there lived a Hobbit."
                }
                """;
        }

        @Language("JSON")
        private static String bookToCreateWithFieldsTooLong() {
            return """
                {
                    "title": "%s",
                    "author": "%s",
                    "description": "%s"
                }
                """.formatted(
                "B".repeat(256),
                "C".repeat(256),
                "D".repeat(5001)
            );
        }

        @Language("JSON")
        private static String expectedBlankValidationProblemJson() {
            return """
                {
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Validation failed",
                  "instance": "/books",
                  "errors": {
                    "title": "must not be blank",
                    "author": "must not be blank",
                    "description": "must not be blank"
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
                  "instance": "/books",
                  "errors": {
                    "title": "size must be between 0 and 255",
                    "author": "size must be between 0 and 255",
                    "description": "size must be between 0 and 5000"
                  }
                }
                """;
        }
    }

    @Nested
    class Update {
        @Test
        void shouldUpdateExistingBook() throws Exception {
            var book = jdbcAggregateTemplate.insert(testBookWithDifferentDescription());

            mvc.perform(put("/books/" + book.id())
                    .contentType(APPLICATION_JSON)
                    .content(bookToUpdate()))
                .andExpect(status().isNoContent());

            assertThat(bookRepository.findAll())
                .singleElement()
                .returns("The Hobbit", from(BookEntity::title))
                .returns("JRR Tolkien", from(BookEntity::author))
                .returns("In a hole under the ground, there lived a Hobbit.", from(BookEntity::description));
        }

        @Test
        void shouldReturnNotFoundWhenUpdatingNonExistentBook() throws Exception {
            var randomId = BookId.randomBookId();
            mvc.perform(put("/books/" + randomId.value())
                    .contentType(APPLICATION_JSON)
                    .content(bookToUpdate()))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedBookNotFoundProblemJsonForUpdate(randomId)));
        }

        @ParameterizedTest
        @MethodSource("invalidBookData")
        void shouldValidateFields(String invalidBookJson, String expectedProblemJson) throws Exception {
            var book = jdbcAggregateTemplate.insert(testBook());
            mvc.perform(put("/books/" + book.id())
                    .contentType(APPLICATION_JSON)
                    .content(invalidBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedProblemJson.formatted(book.id())));
        }

        private static Stream<Arguments> invalidBookData() {
            return Stream.of(
                arguments(bookToUpdateWithNulls(), expectedBlankValidationProblemJson()),
                arguments(bookToUpdateWithBlankStrings(), expectedBlankValidationProblemJson()),
                arguments(bookToUpdateWithWhitespace(), expectedBlankValidationProblemJson()),
                arguments(bookToUpdateWithFieldsTooLong(), expectedMaxLengthValidationProblemJson())
            );
        }

        @Language("JSON")
        private static String bookToUpdateWithNulls() {
            return """
                {
                
                }
                """;
        }

        @Language("JSON")
        private static String bookToUpdateWithBlankStrings() {
            return """
                {
                    "title": "",
                    "author": "",
                    "description": ""
                }
                """;
        }

        @Language("JSON")
        private static String bookToUpdateWithWhitespace() {
            return """
                {
                    "title": "   ",
                    "author": "   ",
                    "description": "   "
                }
                """;
        }

        @Language("JSON")
        private String bookToUpdate() {
            return """
                {
                    "title": "The Hobbit",
                    "author": "JRR Tolkien",
                    "description": "In a hole under the ground, there lived a Hobbit."
                }
                """;
        }

        @Language("JSON")
        private static String bookToUpdateWithFieldsTooLong() {
            return """
                {
                    "title": "%s",
                    "author": "%s",
                    "description": "%s"
                }
                """.formatted(
                "B".repeat(256),
                "C".repeat(256),
                "D".repeat(5001)
            );
        }

        @Language("JSON")
        private static String expectedBlankValidationProblemJson() {
            return """
                {
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Validation failed",
                  "instance": "/books/%s",
                  "errors": {
                    "title": "must not be blank",
                    "author": "must not be blank",
                    "description": "must not be blank"
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
                  "instance": "/books/%s",
                  "errors": {
                    "title": "size must be between 0 and 255",
                    "author": "size must be between 0 and 255",
                    "description": "size must be between 0 and 5000"
                  }
                }
                """;
        }

        @Language("JSON")
        private String expectedBookNotFoundProblemJsonForUpdate(BookId id) {
            return """
                {
                  "title": "Not Found",
                  "status": 404,
                  "detail": "Book %s not found",
                  "instance": "/books/%s"
                }
                """.formatted(id.value(), id.value());
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldDeleteBookWhenBookFoundById() throws Exception {
            var book = jdbcAggregateTemplate.insert(testBook());

            mvc.perform(delete("/books/" + book.id()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
            assertThat(bookRepository.findAll()).isEmpty();
        }

        @Test
        void shouldReturnNotFoundWhenBookNotFoundByIdForDelete() throws Exception {
            var randomId = BookId.randomBookId();
            mvc.perform(delete("/books/" + randomId.value()))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedBookNotFoundProblemJson(randomId)));
        }
    }

    private BookEntity testBook() {
        return new BookEntity(BookId.randomBookId().toUuid(), "The Hobbit", "JRR Tolkien", "In a hole under the ground, there lived a Hobbit.");
    }

    private BookEntity testBookWithDifferentDescription() {
        return new BookEntity(BookId.randomBookId().toUuid(), "The Hobbit", "JRR Tolkien", "INSERT DESCRIPTION HERE");
    }

    @Language("JSON")
    private String expectedBookNotFoundProblemJson(BookId id) {
        return """
            {
              "title": "Not Found",
              "status": 404,
              "detail": "Book %s not found",
              "instance": "/books/%s"
            }
            """.formatted(id.value(), id.value());
    }
}
