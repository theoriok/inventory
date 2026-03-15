package org.theoriok.inventory.command;

import org.theoriok.inventory.BookId;

public interface DeleteBook {
    Result delete(BookId id);

    enum Result {
        DELETED,
        NOT_FOUND
    }
}
