package org.theoriok.inventory.command;

public interface UpsertCap {
    void upsert(Request request);

    final class Request {
        private final String businessId;
        private final String name;
        private final String description;
        private final int amount;
        private final String country;

        public Request(
            String businessId,
            String name,
            String description,
            int amount,
            String country
        ) {
            this.businessId = businessId;
            this.name = name;
            this.description = description;
            this.amount = amount;
            this.country = country;
        }

        public String businessId() {
            return businessId;
        }

        public String name() {
            return name;
        }

        public String description() {
            return description;
        }

        public int amount() {
            return amount;
        }

        public String country() {
            return country;
        }

    }
}
