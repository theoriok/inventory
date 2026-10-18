package org.theoriok.inventory;

public record Amount(int value) {
    public Amount {
        if (value < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
    }
}
