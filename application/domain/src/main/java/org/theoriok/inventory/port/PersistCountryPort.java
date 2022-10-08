package org.theoriok.inventory.port;

import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.domain.Country;

import java.util.Collection;
import java.util.Optional;

public interface PersistCountryPort {
    Collection<Country> findAll();

    Optional<Country> findByCode(String code);
}
