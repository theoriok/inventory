package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Cap(
    String businessId,
    String name,
    String description,
    int amount,
    Country country
) implements CapBuilder.With {
}