package org.theoriok.inventory.persistence.adapters;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.domain.CountryBuilder;
import org.theoriok.inventory.persistence.entities.CountryEntity;
import org.theoriok.inventory.persistence.repositories.CountryRepository;
import org.theoriok.inventory.port.PersistCountryPort;

import java.util.Collection;
import java.util.Optional;

@Component
public class PersistCountryAdapter implements PersistCountryPort {
    private final CountryRepository countryRepository;

    public PersistCountryAdapter(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Collection<Country> findAll() {
        var countries = countryRepository.findAll();
        return countries.stream().map(this::toDomainObject).toList();
    }

    @Override
    public Optional<Country> findByCode(String code) {
        return countryRepository.findByCode(code)
            .map(this::toDomainObject);
    }

    private Country toDomainObject(CountryEntity entity) {
        return CountryBuilder.builder()
            .name(entity.getName())
            .code(entity.getCode())
            .build();
    }
}
