package org.theoriok.inventory.query;

import java.util.List;
import java.util.Optional;

public interface FindCountries {
    ListResponse findAll();

    Optional<SingleResponse> findByCode(String id);

    record ListResponse(List<Country> countries) {
    }

    record SingleResponse(Country country) {
    }

    record Country(String name, String code) {
    }
}
