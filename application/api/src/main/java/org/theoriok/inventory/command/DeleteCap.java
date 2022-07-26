package org.theoriok.inventory.command;

public interface DeleteCap {
    Result delete(String businessId);
    enum Result {
        DELETED,
        NOT_FOUND
    }
}
