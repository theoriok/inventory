package org.theoriok.inventory;

import static java.util.Objects.requireNonNull;

public record CapId(String value) {
    public CapId {
        requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Cap id cannot be blank.");
        }
    }
}
