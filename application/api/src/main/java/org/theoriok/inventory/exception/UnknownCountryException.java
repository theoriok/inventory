package org.theoriok.inventory.exception;

public class UnknownCountryException extends RuntimeException {
    public UnknownCountryException(String code) {
        super("Unknown country %s".formatted(code));
    }
}
