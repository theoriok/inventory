package org.theoriok.inventory.query;

import org.theoriok.inventory.CapId;
import org.theoriok.inventory.domain.Cap;

import java.util.List;
import java.util.Optional;

public interface FindCaps {
    List<Cap> findAll(Request request);

    Optional<Cap> findById(CapId id);

    record Request(String country) {
    }
}
