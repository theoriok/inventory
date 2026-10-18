package org.theoriok.inventory;

import java.util.Objects;

public record Title(String value) {
    public Title {
        Objects.requireNonNull(value, "Title cannot be null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank.");
        }
    }
}
