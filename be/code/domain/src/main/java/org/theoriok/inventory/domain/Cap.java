package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.theoriok.inventory.CapId;

@RecordBuilder
public record Cap(
    CapId businessId,
    String name,
    String description,
    int amount,
    Country country
) implements CapBuilder.With {
    public static Cap create(CapId businessId, String name, String description, int amount, Country country) {
        return CapBuilder.builder()
            .businessId(businessId)
            .name(name)
            .description(description)
            .amount(amount)
            .country(country)
            .build();
    }
}