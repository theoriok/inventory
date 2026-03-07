package org.theoriok.inventory.command;

import org.theoriok.inventory.BookId;

public interface UpdateBook {
    Result update(Request request);

    record Request(BookId id, String title, String author, String description) {
    }

    enum Result {
        UPDATED, NOT_FOUND
    }
}
