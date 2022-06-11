package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Book(
    String businessId,
    String title,
    String author,
    String description
) implements BookBuilder.With {
}
