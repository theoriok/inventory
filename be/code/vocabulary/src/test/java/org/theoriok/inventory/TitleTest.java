package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TitleTest {
    @Test
    void titleShouldNotBeBlank() {
        assertThatThrownBy(() -> new Title(" "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Title cannot be blank.");
    }

    @Test
    void titleShouldNotBeNull() {
        assertThatThrownBy(() -> new Title(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Title cannot be null.");
    }
}
