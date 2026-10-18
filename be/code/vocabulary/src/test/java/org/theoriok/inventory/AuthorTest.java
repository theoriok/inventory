package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AuthorTest {
    @Test
    void authorShouldNotBeBlank() {
        assertThatThrownBy(() -> new Author(" "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Author cannot be blank.");
    }

    @Test
    void authorShouldNotBeNull() {
        assertThatThrownBy(() -> new Author(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Author cannot be null.");
    }
}
