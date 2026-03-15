package org.theoriok.inventory.port;

import org.theoriok.inventory.CapId;
import org.theoriok.inventory.domain.Cap;

import java.util.List;
import java.util.Optional;

public interface PersistCapPort {
    List<Cap> findAll();

    List<Cap> findAllByCountry(String country);

    Optional<Cap> findById(CapId id);

    Cap create(Cap cap);

    void update(Cap cap);

    boolean delete(CapId id);
}
