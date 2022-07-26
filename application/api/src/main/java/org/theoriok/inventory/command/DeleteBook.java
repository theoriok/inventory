package org.theoriok.inventory.command;

public interface DeleteBook {
    Result delete(String businessId);
    enum Result {
        DELETED,
        NOT_FOUND
    }
}
