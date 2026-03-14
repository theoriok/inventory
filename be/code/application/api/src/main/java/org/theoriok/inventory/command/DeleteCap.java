package org.theoriok.inventory.command;

import org.theoriok.inventory.CapId;

public interface DeleteCap {
    Result delete(CapId id);

    enum Result {
        DELETED,
        NOT_FOUND
    }
}
