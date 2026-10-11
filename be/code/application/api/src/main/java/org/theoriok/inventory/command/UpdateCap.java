package org.theoriok.inventory.command;

import org.theoriok.inventory.CapId;

public interface UpdateCap {
    Result update(Request request);

    record Request(CapId id, String name, String description, int amount, String country) {
    }

    sealed interface Result {
        record Updated() implements Result {
        }

        record NotFound(CapId id) implements Result {
        }

        record UnknownCountry(String country) implements Result {
        }
    }
}
