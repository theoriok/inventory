package org.theoriok.inventory.query;

import org.theoriok.inventory.domain.Cap;

import java.util.List;
import java.util.Optional;

public interface FindCaps {
    List<Cap> findAll(Request request);

    Optional<Cap> findById(String businessId);

    record Request(String country) {
    }
}
