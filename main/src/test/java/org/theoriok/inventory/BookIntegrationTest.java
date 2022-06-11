package org.theoriok.inventory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableJpaRepositories
class BookIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturnEmptyArrayWhenNoBookFound() throws Exception {
        mvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }
    @Test
    void shouldReturnCapWhenCapFound() throws Exception {


        mvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJsonArray()));
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
}
