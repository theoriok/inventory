package org.theoriok.inventory.command;

import org.theoriok.inventory.BookId;

public interface UpdateBook {
    Result update(Request request);

    record Request(BookId id, String title, String author, String description) {
    }

    sealed interface Result {
        record Updated() implements Result {
        }

        record NotFound(BookId id) implements Result {
        }
    }
}
