package org.theoriok.inventory.command;

public interface UpsertBook {
    void upsert(Request request);

    record Request(String businessId, String title, String author, String description) {
    }
}
