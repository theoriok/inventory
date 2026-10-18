package org.theoriok.inventory.domain;

import org.theoriok.inventory.Author;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.Description;
import org.theoriok.inventory.Title;

public record Book(BookId id, Title title, Author author, Description description) {
    public static Book create(BookId id, Title title, Author author, Description description) {
        return new Book(id, title, author, description);
    }

    public Book update(Title title, Author author, Description description) {
        return new Book(this.id, title, author, description);
    }
}
