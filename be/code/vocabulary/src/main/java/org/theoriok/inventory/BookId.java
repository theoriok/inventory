package org.theoriok.inventory;

public record BookId(String value) {
    public BookId {
        if (value == null) {
            throw new IllegalArgumentException("Book id cannot be null.");
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException("Book id cannot be blank.");
        }
    }
}
