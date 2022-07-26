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
import org.theoriok.inventory.persistence.entities.BookEntity;
import org.theoriok.inventory.persistence.repositories.BookRepository;

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
                .andExpect(content().json(expectedJsonArray()));
        }

        @Test
        void shouldReturnBookWhenBookFoundById() throws Exception {
            bookRepository.save(testBook());

            mvc.perform(get("/books/BOOK-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonObject()));
        }

        @Test
        void shouldReturnNotFoundWhenBookNotFoundById() throws Exception {
            mvc.perform(get("/books/BOOK-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
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
                .andExpect(content().string(""));
        }
    }

    private BookEntity testBook() {
        return new BookEntity("BOOK-1", "The Hobbit", "JRR Tolkien", "In a hole under the ground, there lived a Hobbit.");
    }

    private BookEntity testBookWithSameIdButDifferentDescription() {
        return new BookEntity("BOOK-1", "The Hobbit", "JRR Tolkien", "INSERT DESCRIPTION HERE");
    }

    @Language("JSON")
    private String expectedJsonArray() {
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
    private String expectedJsonObject() {
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
}
