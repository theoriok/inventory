package org.theoriok.inventory.command;

import org.theoriok.inventory.BookId;

public interface DeleteBook {
    Result delete(BookId businessId);

    enum Result {
        DELETED,
        NOT_FOUND
    }
}
