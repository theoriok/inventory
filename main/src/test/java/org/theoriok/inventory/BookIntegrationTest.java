package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.theoriok.inventory.persistence.entities.BookEntity;
import org.theoriok.inventory.persistence.repositories.BookRepository;

class BookIntegrationTest extends IntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldReturnEmptyArrayWhenNoBookFound() throws Exception {
        mvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnCapWhenCapFound() throws Exception {
        bookRepository.save(testBook());

        mvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJsonArray()));
    }

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

    private BookEntity testBook() {
        return new BookEntity("BOOK-1", "The Hobbit", "JRR Tolkien", "In a hole under the ground, there lived a Hobbit.");
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
