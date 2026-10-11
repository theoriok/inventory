package org.theoriok.inventory.command;

import org.theoriok.inventory.CapId;

public interface DeleteCap {
    Result delete(CapId id);

    sealed interface Result {
        record Deleted() implements Result {
        }

        record NotFound(CapId id) implements Result {
        }
    }
}
