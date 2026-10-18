package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class NameTest {
    @Test
    void nameShouldNotBeBlank() {
        assertThatThrownBy(() -> new Name(" "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Name cannot be blank.");
    }

    @Test
    void nameShouldNotBeNull() {
        assertThatThrownBy(() -> new Name(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Name cannot be null.");
    }
}
