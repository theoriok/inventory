package org.theoriok.inventory;

import static java.util.Objects.requireNonNull;

public record BookId(String value) {
    public BookId {
        requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Book id cannot be blank.");
        }
    }
}
