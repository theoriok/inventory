package org.theoriok.inventory.command;

import org.theoriok.inventory.CapId;

public interface UpsertCap {
    Result upsert(Request request);

    record Request(CapId businessId, String name, String description, int amount, String country) {
    }

    enum Result {
        UPSERTED,
        UNKNOWN_COUNTRY
    }
}
