package org.theoriok.inventory.command;

public interface UpsertCap {
    Result upsert(Request request);

    record Request(String businessId, String name, String description, int amount, String country) {
    }

    enum Result {
        UPSERTED,
        UNKNOWN_COUNTRY
    }
}
