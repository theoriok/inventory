package org.theoriok.inventory.command;

import org.theoriok.inventory.BookId;

public interface DeleteBook {
    Result delete(BookId id);

    sealed interface Result {
        record Deleted() implements Result {
        }

        record NotFound(BookId id) implements Result {
        }
    }
}
