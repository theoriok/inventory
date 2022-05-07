package org.theoriok.inventory.persistence.mappers;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.domain.CountryBuilder;
import org.theoriok.inventory.persistence.entities.CountryEntity;

@Component
public class CountryEntityMapper implements EntityMapper<Country, CountryEntity> {

    @Override
    public CountryEntity toEntity(Country domainObject) {
        return new CountryEntity(domainObject.name(), domainObject.code());
    }

    @Override
    public Country toDomainObject(CountryEntity entity) {
        return CountryBuilder.builder()
            .name(entity.getName())
            .code(entity.getCode())
            .build();
    }
}