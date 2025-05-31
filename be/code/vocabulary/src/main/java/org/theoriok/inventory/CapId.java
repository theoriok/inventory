package org.theoriok.inventory;

public record CapId(String value) {
    public CapId {
        if (value == null) {
            throw new IllegalArgumentException("Cap id cannot be null.");
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException("Cap id cannot be blank.");
        }
    }
}
