package org.theoriok.inventory;

import java.util.Objects;

public record CapId(String value) {
    public CapId {
        Objects.requireNonNull(value, "Cap id cannot be null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Cap id cannot be blank.");
        }
    }
}
