package org.theoriok.inventory.query;

import java.util.List;
import java.util.Optional;

public interface FindCaps {
    ListResponse findAll(Request request);

    Optional<SingleResponse> findById(String businessId);

    record ListResponse(List<Cap> caps) {
    }

    record Request(String country) {
    }

    record SingleResponse(Cap cap) {
    }

    record Cap(String businessId, String name, String description, int amount, Country country) {
    }

    record Country(String name, String code) {
    }
}
