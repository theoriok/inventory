package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.theoriok.inventory.CapId;

@RecordBuilder
public record Cap(
    CapId id,
    String name,
    String description,
    int amount,
    Country country
) implements CapBuilder.With {
    public static Cap create(CapId id, String name, String description, int amount, Country country) {
        return CapBuilder.builder()
            .id(id)
            .name(name)
            .description(description)
            .amount(amount)
            .country(country)
            .build();
    }

    public Cap update(String name, String description, int amount, Country country) {
        return CapBuilder.builder()
            .id(this.id)
            .name(name)
            .description(description)
            .amount(amount)
            .country(country)
            .build();
    }
}