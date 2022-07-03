package org.theoriok.inventory.port;

import org.theoriok.inventory.domain.Country;

import java.util.Optional;

public interface PersistCountryPort {
    Optional<Country> findByCode(String code);
}
