package org.theoriok.inventory.query;

import org.theoriok.inventory.domain.Country;

import java.util.List;
import java.util.Optional;

public interface FindCountries {
    List<Country> findAll();

    Optional<Country> findByCode(String id);

}
