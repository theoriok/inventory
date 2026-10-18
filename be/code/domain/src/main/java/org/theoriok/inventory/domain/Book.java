package org.theoriok.inventory.domain;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.theoriok.inventory.Author;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.Description;
import org.theoriok.inventory.Title;

@RecordBuilder
public record Book(
    BookId id,
    Title title,
    Author author,
    Description description
) implements BookBuilder.With {
    public static Book create(BookId id, Title title, Author author, Description description) {
        return BookBuilder.builder()
            .id(id)
            .title(title)
            .author(author)
            .description(description)
            .build();
    }

    public Book update(Title title, Author author, Description description) {
        return BookBuilder.builder()
            .id(this.id)
            .title(title)
            .author(author)
            .description(description)
            .build();
    }
}
