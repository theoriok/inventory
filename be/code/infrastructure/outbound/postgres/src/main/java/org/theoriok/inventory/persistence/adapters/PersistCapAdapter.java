package org.theoriok.inventory.persistence.adapters;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Component;
import org.theoriok.inventory.CapId;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.domain.CapBuilder;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.domain.CountryBuilder;
import org.theoriok.inventory.persistence.entities.CapEntity;
import org.theoriok.inventory.persistence.entities.CountryEntity;
import org.theoriok.inventory.persistence.repositories.CapRepository;
import org.theoriok.inventory.persistence.repositories.CountryRepository;
import org.theoriok.inventory.port.PersistCapPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class PersistCapAdapter implements PersistCapPort {
    private final CapRepository capRepository;
    private final CountryRepository countryRepository;
    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    public PersistCapAdapter(CapRepository capRepository, CountryRepository countryRepository, JdbcAggregateTemplate jdbcAggregateTemplate) {
        this.capRepository = capRepository;
        this.countryRepository = countryRepository;
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
    }

    @Override
    public List<Cap> findAll() {
        List<CapEntity> capEntities = capRepository.findAll();
        if (capEntities.isEmpty()) {
            return emptyList();
        }
        Set<String> distinctCountryCodes = capEntities.stream()
            .map(CapEntity::countryCode)
            .collect(toSet());
        Map<String, Country> countriesByCode = countryRepository.findAllByCodeIn(distinctCountryCodes).stream()
            .collect(toMap(CountryEntity::code, this::toDomainObject));
        return toDomainObjects(capEntities, countriesByCode);
    }

    @Override
    public List<Cap> findAllByCountry(String country) {
        List<CapEntity> capEntities = capRepository.findAllByCountryCode(country);
        if (capEntities.isEmpty()) {
            return emptyList();
        }
        Map<String, Country> countriesByCode = Map.of(country, getCountry(country));
        return toDomainObjects(capEntities, countriesByCode);
    }

    @Override
    public Optional<Cap> findById(CapId id) {
        return capRepository.findById(id.toUuid())
            .map(entity -> toDomainObject(entity, getCountry(entity.countryCode())));
    }

    private Country getCountry(String country) {
        return countryRepository.findByCode(country)
            .map(this::toDomainObject)
            .orElseThrow();
    }

    @Override
    public Cap create(Cap cap) {
        jdbcAggregateTemplate.insert(toEntity(cap));
        return cap;
    }

    @Override
    public void update(Cap cap) {
        jdbcAggregateTemplate.update(toEntity(cap));
    }

    @Override
    public boolean delete(CapId id) {
        boolean capExists = capRepository.existsById(id.toUuid());
        if (capExists) {
            capRepository.deleteById(id.toUuid());
        }
        return capExists;
    }

    private CapEntity toEntity(Cap domainObject) {
        return new CapEntity(
            domainObject.id().toUuid(),
            domainObject.name(),
            domainObject.description(),
            domainObject.amount(),
            domainObject.country().code()
        );
    }

    private List<Cap> toDomainObjects(List<CapEntity> entities, Map<String, Country> countriesByCode) {
        return entities.stream()
            .map(entity -> toDomainObject(entity, countriesByCode.get(entity.countryCode())))
            .toList();
    }

    private Cap toDomainObject(CapEntity entity, Country country) {
        return CapBuilder.builder()
            .id(CapId.from(entity.id()))
            .name(entity.name())
            .description(entity.description())
            .amount(entity.amount())
            .country(country)
            .build();
    }

    private Country toDomainObject(CountryEntity entity) {
        return CountryBuilder.builder()
            .name(entity.name())
            .code(entity.code())
            .build();
    }
}
