package org.theoriok.inventory.command;

import org.theoriok.inventory.BookId;
import org.theoriok.inventory.domain.Book;

public interface CreateBook {
    Book create(Request request);

    record Request(BookId id, String title, String author, String description) {
    }
}
