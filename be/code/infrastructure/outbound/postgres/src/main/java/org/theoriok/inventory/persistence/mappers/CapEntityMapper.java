package org.theoriok.inventory.persistence.mappers;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.domain.CapBuilder;
import org.theoriok.inventory.persistence.entities.CapEntity;

@Component
public class CapEntityMapper implements EntityMapper<Cap, CapEntity> {
    private final CountryEntityMapper countryEntityMapper;

    public CapEntityMapper(CountryEntityMapper countryEntityMapper) {
        this.countryEntityMapper = countryEntityMapper;
    }

    @Override
    public CapEntity toEntity(Cap domainObject) {
        return new CapEntity(
            domainObject.businessId(),
            domainObject.name(),
            domainObject.description(),
            domainObject.amount(),
            countryEntityMapper.toEntity(domainObject.country())
        );
    }

    @Override
    public Cap toDomainObject(CapEntity entity) {
        return CapBuilder.builder()
            .businessId(entity.getBusinessId())
            .name(entity.getName())
            .description(entity.getDescription())
            .amount(entity.getAmount())
            .country(countryEntityMapper.toDomainObject(entity.getCountry()))
            .build();
    }
}
