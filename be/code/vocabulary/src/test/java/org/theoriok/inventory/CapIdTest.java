package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CapIdTest {
    @Test
    void capIdShouldNotBeBlank() {
        assertThatThrownBy(() -> new CapId(" "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cap id cannot be blank.");
    }

    @Test
    void capIdShouldNotBeNull() {
        assertThatThrownBy(() -> new CapId(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cap id cannot be null.");
    }
}