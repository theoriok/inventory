package org.theoriok.inventory.query;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.port.PersistCountryPort;

import java.util.List;
import java.util.Optional;

@Component
public class FindCountriesQuery implements FindCountries {
    private final PersistCountryPort persistCountryPort;

    public FindCountriesQuery(PersistCountryPort persistCountryPort) {
        this.persistCountryPort = persistCountryPort;
    }

    @Override
    public List<Country> findAll() {
        var countries = persistCountryPort.findAll();
        return countries.stream().toList();
    }

    @Override
    public Optional<Country> findByCode(String code) {
        return persistCountryPort.findByCode(code);
    }
}
