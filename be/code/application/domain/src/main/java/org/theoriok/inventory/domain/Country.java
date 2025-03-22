package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Country(
    String name,
    String code
) implements CountryBuilder.With {
}