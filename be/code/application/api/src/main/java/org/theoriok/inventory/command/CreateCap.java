package org.theoriok.inventory.command;

import org.theoriok.inventory.CapId;
import org.theoriok.inventory.domain.Cap;

public interface CreateCap {
    Result create(Request request);

    record Request(CapId id, String name, String description, int amount, String country) {
    }

    sealed interface Result {
        record Created(Cap cap) implements Result {
        }

        record UnknownCountry() implements Result {
        }
    }
}
