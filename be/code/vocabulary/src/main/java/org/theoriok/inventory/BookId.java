package org.theoriok.inventory;

import java.util.Objects;

public record BookId(String value) {
    public BookId {
        Objects.requireNonNull(value, "Book id cannot be null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Book id cannot be blank.");
        }
    }
}
