package org.theoriok.inventory;

import java.util.Objects;

public record Author(String value) {
    public Author {
        Objects.requireNonNull(value, "Author cannot be null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Author cannot be blank.");
        }
    }
}
