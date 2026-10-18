package org.theoriok.inventory;

import java.util.Objects;
import java.util.regex.Pattern;

public record CountryCode(String value) {
    private static final Pattern ALPHA_2 = Pattern.compile("^[A-Z]{2}$");

    public CountryCode {
        Objects.requireNonNull(value, "Country code cannot be null.");
        if (!ALPHA_2.matcher(value).matches()) {
            throw new IllegalArgumentException("Country code must be two uppercase letters.");
        }
    }
}
