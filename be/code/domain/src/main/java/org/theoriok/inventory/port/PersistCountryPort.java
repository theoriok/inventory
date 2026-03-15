package org.theoriok.inventory.port;

import org.theoriok.inventory.domain.Country;

import java.util.List;
import java.util.Optional;

public interface PersistCountryPort {
    List<Country> findAll();

    Optional<Country> findByCode(String code);
}
