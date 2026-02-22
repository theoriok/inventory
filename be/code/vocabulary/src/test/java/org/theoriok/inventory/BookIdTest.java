package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BookIdTest {
    @Test
    void bookIdShouldNotBeBlank() {
        assertThatThrownBy(() -> new BookId(" "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Book id cannot be blank.");
    }

    @Test
    void bookIdShouldNotBeNull() {
        assertThatThrownBy(() -> new BookId(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Book id cannot be null.");
    }
}