package org.theoriok.inventory;

import java.util.Objects;

public record Description(String value) {
    public Description {
        Objects.requireNonNull(value, "Description cannot be null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank.");
        }
    }
}
