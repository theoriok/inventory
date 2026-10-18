package org.theoriok.inventory.persistence.adapters;

import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import org.theoriok.inventory.CountryCode;
import org.theoriok.inventory.Name;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.persistence.entities.CountryEntity;
import org.theoriok.inventory.port.PersistCountryPort;

import java.util.List;
import java.util.Optional;

@Component
public class PersistCountryAdapter implements PersistCountryPort {
    private final JdbcAggregateOperations jdbcAggregateTemplate;

    public PersistCountryAdapter(JdbcAggregateOperations jdbcAggregateTemplate) {
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
    }

    @Override
    public List<Country> findAll() {
        return jdbcAggregateTemplate.findAll(CountryEntity.class).stream()
            .map(this::toDomainObject)
            .toList();
    }

    @Override
    public Optional<Country> findByCode(CountryCode code) {
        return jdbcAggregateTemplate.findOne(Query.query(Criteria.where("code").is(code.value())), CountryEntity.class)
            .map(this::toDomainObject);
    }

    private Country toDomainObject(CountryEntity entity) {
        return new Country(
            new Name(entity.name()),
            new CountryCode(entity.code())
        );
    }
}
