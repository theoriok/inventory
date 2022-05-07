package org.theoriok.inventory.command;

public interface UpsertCap {
    void upsert(Request request);

    record Request(String businessId, String name, String description, int amount, String country) {
    }
}
