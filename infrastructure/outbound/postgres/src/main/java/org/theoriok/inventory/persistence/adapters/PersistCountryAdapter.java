package org.theoriok.inventory.persistence.adapters;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.persistence.mappers.CountryEntityMapper;
import org.theoriok.inventory.persistence.repositories.CountryRepository;
import org.theoriok.inventory.port.PersistCountryPort;

import java.util.Optional;

@Component
public class PersistCountryAdapter implements PersistCountryPort {
    private final CountryRepository countryRepository;
    private final CountryEntityMapper countryDomainMapper;

    public PersistCountryAdapter(CountryRepository countryRepository, CountryEntityMapper countryDomainMapper) {
        this.countryRepository = countryRepository;
        this.countryDomainMapper = countryDomainMapper;
    }

    @Override
    public Optional<Country> findByCode(String code) {
        return countryRepository.findByCode(code)
            .map(countryDomainMapper::toDomainObject);
    }
}
