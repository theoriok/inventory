package org.theoriok.inventory.command;

import org.theoriok.inventory.Author;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.Description;
import org.theoriok.inventory.Title;

public interface UpdateBook {
    Result update(Request request);

    record Request(BookId id, Title title, Author author, Description description) {
    }

    sealed interface Result {
        record Updated() implements Result {
        }

        record NotFound(BookId id) implements Result {
        }
    }
}
