package org.theoriok.inventory.command;

import org.theoriok.inventory.CapId;

public interface CreateCap {
    Result create(Request request);

    record Request(CapId id, String name, String description, int amount, String country) {
    }

    enum Result {
        CREATED,
        UNKNOWN_COUNTRY
    }
}
