package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.theoriok.inventory.BookId;

@RecordBuilder
public record Book(
    BookId businessId,
    String title,
    String author,
    String description
) implements BookBuilder.With {
}
