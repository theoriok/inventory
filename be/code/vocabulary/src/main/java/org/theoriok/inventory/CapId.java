package org.theoriok.inventory;

import java.util.Objects;
import java.util.UUID;

public record CapId(String value) {
    public CapId {
        Objects.requireNonNull(value, "Cap id cannot be null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Cap id cannot be blank.");
        }
    }

    public static CapId randomCapId() {
        return CapId.from(UUID.randomUUID());
    }

    public static CapId from(UUID id) {
        return new CapId(id.toString());
    }

    public UUID toUuid() {
        return UUID.fromString(value);
    }
}
