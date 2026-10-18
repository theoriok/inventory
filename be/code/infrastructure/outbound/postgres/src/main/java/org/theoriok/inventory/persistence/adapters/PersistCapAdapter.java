package org.theoriok.inventory.persistence.adapters;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import org.theoriok.inventory.Amount;
import org.theoriok.inventory.CapId;
import org.theoriok.inventory.CountryCode;
import org.theoriok.inventory.Description;
import org.theoriok.inventory.Name;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.domain.CapBuilder;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.domain.CountryBuilder;
import org.theoriok.inventory.persistence.entities.CapEntity;
import org.theoriok.inventory.persistence.entities.CountryEntity;
import org.theoriok.inventory.persistence.repositories.CapRepository;
import org.theoriok.inventory.port.PersistCapPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class PersistCapAdapter implements PersistCapPort {
    private final CapRepository capRepository;
    private final JdbcAggregateOperations jdbcAggregateTemplate;

    public PersistCapAdapter(CapRepository capRepository, JdbcAggregateOperations jdbcAggregateTemplate) {
        this.capRepository = capRepository;
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
    }

    @Override
    public List<Cap> findAll() {
        List<CapEntity> capEntities = jdbcAggregateTemplate.findAll(CapEntity.class);
        if (capEntities.isEmpty()) {
            return emptyList();
        }
        Set<String> distinctCountryCodes = capEntities.stream()
            .map(CapEntity::countryCode)
            .collect(toSet());
        Map<String, Country> countriesByCode = jdbcAggregateTemplate.findAll(Query.query(Criteria.where("code").in(distinctCountryCodes)), CountryEntity.class)
            .stream()
            .collect(toMap(CountryEntity::code, this::toDomainObject));
        return toDomainObjects(capEntities, countriesByCode);
    }

    @Override
    public List<Cap> findAllByCountry(CountryCode country) {
        List<CapEntity> capEntities = jdbcAggregateTemplate.findAll(Query.query(Criteria.where("country_code").is(country.value())), CapEntity.class);
        if (capEntities.isEmpty()) {
            return emptyList();
        }
        Map<String, Country> countriesByCode = Map.of(country.value(), getCountry(country.value()));
        return toDomainObjects(capEntities, countriesByCode);
    }

    @Override
    public Optional<Cap> findById(CapId id) {
        return jdbcAggregateTemplate.findOne(Query.query(Criteria.where("id").is(id.toUuid())), CapEntity.class)
            .map(entity -> toDomainObject(entity, getCountry(entity.countryCode())));
    }

    private Country getCountry(String country) {
        return jdbcAggregateTemplate.findOne(Query.query(Criteria.where("code").is(country)), CountryEntity.class)
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
        return capRepository.deleteByIdReturningCount(id.toUuid()) > 0;
    }

    private CapEntity toEntity(Cap cap) {
        return new CapEntity(
            cap.id().toUuid(),
            cap.name().value(),
            cap.description().value(),
            cap.amount().value(),
            cap.country().code().value()
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
            .name(new Name(entity.name()))
            .description(new Description(entity.description()))
            .amount(new Amount(entity.amount()))
            .country(country)
            .build();
    }

    private Country toDomainObject(CountryEntity entity) {
        return CountryBuilder.builder()
            .name(new Name(entity.name()))
            .code(new CountryCode(entity.code()))
            .build();
    }
}
