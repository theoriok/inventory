package org.theoriok.inventory.persistence.adapters;

import org.springframework.stereotype.Component;
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

    public PersistCapAdapter(CapRepository capRepository, CountryRepository countryRepository) {
        this.capRepository = capRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public Collection<Cap> findAll() {
        var capEntities = capRepository.findAll();
        return toDomainObjects(capEntities);
    }

    @Override
    public Collection<Cap> findAllByCountry(String country) {
        var capEntities = capRepository.findAllByCountry_code(country);
        return toDomainObjects(capEntities);
    }

    @Override
    public Optional<Cap> findById(String businessId) {
        return capRepository.findByBusinessId(businessId)
            .map(this::toDomainObject);
    }

    @Override
    public void upsert(Cap cap) {
        var entity = toEntity(cap);
        capRepository.findByBusinessId(cap.businessId()).ifPresent(foundEntity -> entity.setId(foundEntity.getId()));
        countryRepository.findByCode(cap.country().code()).ifPresentOrElse(entity::setCountry, () -> countryRepository.save(entity.getCountry()));
        capRepository.save(entity);
    }

    @Override
    public void delete(Cap cap) {
        capRepository.delete(capRepository.findByBusinessId(cap.businessId()).orElseThrow());
    }

    private CapEntity toEntity(Cap domainObject) {
        return new CapEntity(
            domainObject.businessId(),
            domainObject.name(),
            domainObject.description(),
            domainObject.amount(),
            toEntity(domainObject.country())
        );
    }

private    CountryEntity toEntity(Country domainObject) {
        return new CountryEntity(domainObject.name(), domainObject.code());
    }

    Collection<Cap> toDomainObjects(Collection<CapEntity> entities) {
        return entities.stream()
            .map(this::toDomainObject)
            .toList();
    }

    private Cap toDomainObject(CapEntity entity) {
        return CapBuilder.builder()
            .businessId(entity.getBusinessId())
            .name(entity.getName())
            .description(entity.getDescription())
            .amount(entity.getAmount())
            .country(toDomainObject(entity.getCountry()))
            .build();
    }

    private Country toDomainObject(CountryEntity entity) {
        return CountryBuilder.builder()
            .name(entity.getName())
            .code(entity.getCode())
            .build();
    }
}
