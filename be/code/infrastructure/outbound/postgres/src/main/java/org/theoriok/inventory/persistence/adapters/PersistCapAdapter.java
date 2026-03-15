package org.theoriok.inventory.persistence.adapters;

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

import java.util.Collection;
import java.util.Optional;

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
    public Collection<Cap> findAll() {
        return toDomainObjects(capRepository.findAll());
    }

    @Override
    public Collection<Cap> findAllByCountry(String country) {
        return toDomainObjects(capRepository.findAllByCountryCode(country));
    }

    @Override
    public Optional<Cap> findById(CapId id) {
        return capRepository.findById(id.toUuid())
            .map(this::toDomainObject);
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
        Optional<CapEntity> capEntity = capRepository.findById(id.toUuid());
        capEntity.ifPresent(capRepository::delete);
        return capEntity.isPresent();
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

    private Collection<Cap> toDomainObjects(Collection<CapEntity> entities) {
        return entities.stream()
            .map(this::toDomainObject)
            .toList();
    }

    private Cap toDomainObject(CapEntity entity) {
        var country = countryRepository.findByCode(entity.countryCode()).orElseThrow();
        return CapBuilder.builder()
            .id(CapId.from(entity.id()))
            .name(entity.name())
            .description(entity.description())
            .amount(entity.amount())
            .country(toDomainObject(country))
            .build();
    }

    private Country toDomainObject(CountryEntity entity) {
        return CountryBuilder.builder()
            .name(entity.name())
            .code(entity.code())
            .build();
    }
}
