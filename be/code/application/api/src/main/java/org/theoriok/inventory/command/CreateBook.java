package org.theoriok.inventory.command;

import org.theoriok.inventory.Author;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.Description;
import org.theoriok.inventory.Title;
import org.theoriok.inventory.domain.Book;

public interface CreateBook {
    Book create(Request request);

    record Request(BookId id, Title title, Author author, Description description) {
    }
}
