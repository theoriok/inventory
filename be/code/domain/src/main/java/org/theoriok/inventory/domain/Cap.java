package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.theoriok.inventory.Amount;
import org.theoriok.inventory.CapId;
import org.theoriok.inventory.Description;
import org.theoriok.inventory.Name;

@RecordBuilder
public record Cap(
    CapId id,
    Name name,
    Description description,
    Amount amount,
    Country country
) implements CapBuilder.With {
    public static Cap create(CapId id, Name name, Description description, Amount amount, Country country) {
        return CapBuilder.builder()
            .id(id)
            .name(name)
            .description(description)
            .amount(amount)
            .country(country)
            .build();
    }

    public Cap update(Name name, Description description, Amount amount, Country country) {
        return CapBuilder.builder()
            .id(this.id)
            .name(name)
            .description(description)
            .amount(amount)
            .country(country)
            .build();
    }
}
