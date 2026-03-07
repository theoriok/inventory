package org.theoriok.inventory;

import java.util.Objects;
import java.util.UUID;

public record BookId(String value) {
    public BookId {
        Objects.requireNonNull(value, "Book id cannot be null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Book id cannot be blank.");
        }
    }

    public static BookId randomBookId() {
        return  BookId.from(UUID.randomUUID());
    }

    public static BookId from(UUID id) {
        return new BookId(id.toString());
    }

    public UUID toUuid() {
        return UUID.fromString(value);
    }
}
