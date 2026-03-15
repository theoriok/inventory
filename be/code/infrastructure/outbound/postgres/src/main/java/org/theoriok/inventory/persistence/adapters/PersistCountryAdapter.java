package org.theoriok.inventory.persistence.adapters;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.domain.CountryBuilder;
import org.theoriok.inventory.persistence.entities.CountryEntity;
import org.theoriok.inventory.persistence.repositories.CountryRepository;
import org.theoriok.inventory.port.PersistCountryPort;

import java.util.List;
import java.util.Optional;

@Component
public class PersistCountryAdapter implements PersistCountryPort {
    private final CountryRepository countryRepository;

    public PersistCountryAdapter(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll().stream()
            .map(this::toDomainObject)
            .toList();
    }

    @Override
    public Optional<Country> findByCode(String code) {
        return countryRepository.findByCode(code)
            .map(this::toDomainObject);
    }

    private Country toDomainObject(CountryEntity entity) {
        return CountryBuilder.builder()
            .name(entity.name())
            .code(entity.code())
            .build();
    }
}
