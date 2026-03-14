package org.theoriok.inventory.command;

import org.theoriok.inventory.CapId;

public interface UpdateCap {
    Result update(Request request);

    record Request(CapId id, String name, String description, int amount, String country) {
    }

    enum Result {
        UPDATED,
        NOT_FOUND,
        UNKNOWN_COUNTRY
    }
}
