package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class DescriptionTest {
    @Test
    void descriptionShouldNotBeBlank() {
        assertThatThrownBy(() -> new Description(" "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Description cannot be blank.");
    }

    @Test
    void descriptionShouldNotBeNull() {
        assertThatThrownBy(() -> new Description(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Description cannot be null.");
    }
}
