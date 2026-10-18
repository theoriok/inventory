package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CountryCodeTest {
    @Test
    void countryCodeShouldNotBeNull() {
        assertThatThrownBy(() -> new CountryCode(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Country code cannot be null.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "B", "BEL", "be", "B1", "12"})
    void countryCodeShouldBeTwoUppercaseLetters(String invalid) {
        assertThatThrownBy(() -> new CountryCode(invalid))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Country code must be two uppercase letters.");
    }
}
