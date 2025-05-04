package org.theoriok.inventory.command;

import org.theoriok.inventory.BookId;

public interface UpsertBook {
    void upsert(Request request);

    record Request(BookId businessId, String title, String author, String description) {
    }
}
