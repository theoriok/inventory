package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.theoriok.inventory.BookId;

@RecordBuilder
public record Book(
    BookId id,
    String title,
    String author,
    String description
) implements BookBuilder.With {
    public static Book create(BookId id, String title, String author, String description) {
        return BookBuilder.builder()
            .id(id)
            .title(title)
            .author(author)
            .description(description)
            .build();
    }

    public Book update(String title, String author, String description) {
        return BookBuilder.builder()
            .id(this.id)
            .title(title)
            .author(author)
            .description(description)
            .build();
    }
}
