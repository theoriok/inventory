package org.theoriok.inventory.query;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.mappers.CountryCommandMapper;
import org.theoriok.inventory.port.PersistCountryPort;

import java.util.Optional;

@Component
public class FindCountriesQuery implements FindCountries {
    private final PersistCountryPort persistCountryPort;

    private final CountryCommandMapper countryCommandMapper;

    public FindCountriesQuery(PersistCountryPort persistCountryPort, CountryCommandMapper countryCommandMapper) {
        this.persistCountryPort = persistCountryPort;
        this.countryCommandMapper = countryCommandMapper;
    }

    @Override
    public ListResponse findAll() {
        var countries = persistCountryPort.findAll();
        return countryCommandMapper.toListResponse(countries);
    }

    @Override
    public Optional<SingleResponse> findByCode(String code) {
        return persistCountryPort.findByCode(code).map(countryCommandMapper::toSingleResponse);
    }
}
