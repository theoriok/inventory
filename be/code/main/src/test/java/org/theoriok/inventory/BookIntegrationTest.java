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
            bookRepository.save(testBook());

            mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBooks()));
        }

        @Test
        void shouldReturnBookWhenBookFoundById() throws Exception {
            bookRepository.save(testBook());

            mvc.perform(get("/books/BOOK-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBook()));
        }

        @Test
        void shouldReturnNotFoundWhenBookNotFoundById() throws Exception {
            mvc.perform(get("/books/BOOK-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedBookNotFoundProblemJson()));
        }

        @Language("JSON")
        private String expectedBooks() {
            return """
            [
                {
                    "business_id": "BOOK-1",
                    "title": "The Hobbit",
                    "author": "JRR Tolkien",
                    "description": "In a hole under the ground, there lived a Hobbit."
                }
            ]
            """;
        }

        @Language("JSON")
        private String expectedBook() {
            return """
            {
                "business_id": "BOOK-1",
                "title": "The Hobbit",
                "author": "JRR Tolkien",
                "description": "In a hole under the ground, there lived a Hobbit."
            }
            """;
        }
    }

    @Nested
    class Upsert {
        @Test
        void shouldInsertNewBook() throws Exception {
            mvc.perform(put("/books")
                    .contentType(APPLICATION_JSON)
                    .content(bookToUpsert()))
                .andExpect(status().isNoContent());

            assertThat(bookRepository.findAll())
                .singleElement()
                .returns("BOOK-1", from(BookEntity::getBusinessId))
                .returns("The Hobbit", from(BookEntity::getTitle))
                .returns("JRR Tolkien", from(BookEntity::getAuthor))
                .returns("In a hole under the ground, there lived a Hobbit.", from(BookEntity::getDescription));
        }

        @Test
        void shouldUpdateExistingBook() throws Exception {
            bookRepository.save(testBookWithSameIdButDifferentDescription());

            mvc.perform(put("/books")
                    .contentType(APPLICATION_JSON)
                    .content(bookToUpsert()))
                .andExpect(status().isNoContent());

            assertThat(bookRepository.findAll())
                .singleElement()
                .returns("BOOK-1", from(BookEntity::getBusinessId))
                .returns("The Hobbit", from(BookEntity::getTitle))
                .returns("JRR Tolkien", from(BookEntity::getAuthor))
                .returns("In a hole under the ground, there lived a Hobbit.", from(BookEntity::getDescription));
        }

        @ParameterizedTest
        @MethodSource("invalidBookData")
        void shouldValidateFields(String invalidBookJson, String expectedProblemJson) throws Exception {
            mvc.perform(put("/books")
                    .contentType(APPLICATION_JSON)
                    .content(invalidBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedProblemJson));
        }

        private static Stream<Arguments> invalidBookData() {
            return Stream.of(
                arguments(bookToUpsertWithNulls(), expectedBlankValidationProblemJson()),
                arguments(bookToUpsertWithBlankStrings(), expectedBlankValidationProblemJson()),
                arguments(bookToUpsertWithWhitespace(), expectedBlankValidationProblemJson()),
                arguments(bookToUpsertWithFieldsTooLong(), expectedMaxLengthValidationProblemJson())
            );
        }

        @Language("JSON")
        private static String bookToUpsertWithNulls() {
            return """
                {
                
                }
                """;
        }

        @Language("JSON")
        private static String bookToUpsertWithBlankStrings() {
            return """
                {
                    "business_id": "",
                    "title": "",
                    "author": "",
                    "description": ""
                }
                """;
        }

        @Language("JSON")
        private static String bookToUpsertWithWhitespace() {
            return """
                {
                    "business_id": "   ",
                    "title": "   ",
                    "author": "   ",
                    "description": "   "
                }
                """;
        }

        @Language("JSON")
        private String bookToUpsert() {
            return """
                {
                    "business_id": "BOOK-1",
                    "title": "The Hobbit",
                    "author": "JRR Tolkien",
                    "description": "In a hole under the ground, there lived a Hobbit."
                }
                """;
        }

        @Language("JSON")
        private static String bookToUpsertWithFieldsTooLong() {
            return """
                {
                    "business_id": "%s",
                    "title": "%s",
                    "author": "%s",
                    "description": "%s"
                }
                """.formatted(
                "A".repeat(256),
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
                "businessId": "must not be blank",
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
                "businessId": "size must be between 0 and 255",
                "title": "size must be between 0 and 255",
                "author": "size must be between 0 and 255",
                "description": "size must be between 0 and 5000"
              }
            }
            """;
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldDeleteBookWhenBookFoundById() throws Exception {
            bookRepository.save(testBook());

            mvc.perform(delete("/books/BOOK-1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
            assertThat(bookRepository.findAll()).isEmpty();
        }

        @Test
        void shouldReturnNotFoundWhenBookNotFoundByIdForDelete() throws Exception {
            mvc.perform(delete("/books/BOOK-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedBookNotFoundProblemJson()));
        }
    }

    private BookEntity testBook() {
        return new BookEntity("BOOK-1", "The Hobbit", "JRR Tolkien", "In a hole under the ground, there lived a Hobbit.");
    }

    private BookEntity testBookWithSameIdButDifferentDescription() {
        return new BookEntity("BOOK-1", "The Hobbit", "JRR Tolkien", "INSERT DESCRIPTION HERE");
    }

    @Language("JSON")
    private String expectedBookNotFoundProblemJson() {
        return """
            {
              "title": "Not Found",
              "status": 404,
              "instance": "/books/BOOK-1"
            }
            """;
    }
}
