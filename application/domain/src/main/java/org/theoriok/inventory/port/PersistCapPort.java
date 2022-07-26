package org.theoriok.inventory.port;

import org.theoriok.inventory.domain.Cap;

import java.util.Collection;
import java.util.Optional;

public interface PersistCapPort {
    Collection<Cap> findAll();

    Collection<Cap> findAllByCountry(String country);

    Optional<Cap> findById(String businessId);

    void upsert(Cap cap);

    void delete(String businessId);
}
