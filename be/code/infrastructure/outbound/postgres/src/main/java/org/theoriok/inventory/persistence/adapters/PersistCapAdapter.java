package org.theoriok.inventory.persistence.adapters;

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

    public PersistCapAdapter(CapRepository capRepository, CountryRepository countryRepository) {
        this.capRepository = capRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public Collection<Cap> findAll() {
        return toDomainObjects(capRepository.findAll());
    }

    @Override
    public Collection<Cap> findAllByCountry(String country) {
        return toDomainObjects(capRepository.findAllByCountry_code(country));
    }

    @Override
    public Optional<Cap> findById(CapId id) {
        return capRepository.findById(id.toUuid())
            .map(this::toDomainObject);
    }

    @Override
    public Cap create(Cap cap) {
        var entity = toEntity(cap);
        countryRepository.findByCode(cap.country().code()).ifPresent(entity::setCountry);
        capRepository.save(entity);
        return cap;
    }

    @Override
    public void update(Cap cap) {
        var entity = toEntity(cap);
        countryRepository.findByCode(cap.country().code()).ifPresent(entity::setCountry);
        capRepository.save(entity);
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
            toEntity(domainObject.country())
        );
    }

    private CountryEntity toEntity(Country domainObject) {
        return new CountryEntity(domainObject.name(), domainObject.code());
    }

    private Collection<Cap> toDomainObjects(Collection<CapEntity> entities) {
        return entities.stream()
            .map(this::toDomainObject)
            .toList();
    }

    private Cap toDomainObject(CapEntity entity) {
        return CapBuilder.builder()
            .id(CapId.from(entity.getId()))
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
