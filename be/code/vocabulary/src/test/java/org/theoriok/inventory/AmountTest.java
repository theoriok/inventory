package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AmountTest {
    @Test
    void amountShouldNotBeNegative() {
        assertThatThrownBy(() -> new Amount(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Amount cannot be negative.");
    }

    @Test
    void amountOfZeroIsAllowed() {
        assertThat(new Amount(0).value()).isZero();
    }
}
