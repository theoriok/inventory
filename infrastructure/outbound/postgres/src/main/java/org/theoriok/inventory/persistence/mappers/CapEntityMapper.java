package org.theoriok.inventory.persistence.mappers;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.domain.CapBuilder;
import org.theoriok.inventory.persistence.entities.CapEntity;

@Component
public class CapEntityMapper implements EntityMapper<Cap, CapEntity> {
    private final CountryEntityMapper countryDomainMapper;

    public CapEntityMapper(CountryEntityMapper countryDomainMapper) {
        this.countryDomainMapper = countryDomainMapper;
    }

    @Override
    public CapEntity toEntity(Cap domainObject) {
        return new CapEntity(
            domainObject.businessId(),
            domainObject.name(),
            domainObject.description(),
            domainObject.amount(),
            countryDomainMapper.toEntity(domainObject.country())
        );
    }

    @Override
    public Cap toDomainObject(CapEntity entity) {
        return CapBuilder.builder()
            .businessId(entity.getBusinessId())
            .name(entity.getName())
            .description(entity.getDescription())
            .amount(entity.getAmount())
            .country(countryDomainMapper.toDomainObject(entity.getCountry()))
            .build();
    }
}
