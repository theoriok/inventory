package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.theoriok.inventory.CountryCode;
import org.theoriok.inventory.Name;

@RecordBuilder
public record Country(
    Name name,
    CountryCode code
) implements CountryBuilder.With {
}
