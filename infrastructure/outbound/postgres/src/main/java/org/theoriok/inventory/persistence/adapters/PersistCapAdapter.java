package org.theoriok.inventory.persistence.adapters;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.persistence.mappers.CapEntityMapper;
import org.theoriok.inventory.persistence.repositories.CapRepository;
import org.theoriok.inventory.persistence.repositories.CountryRepository;
import org.theoriok.inventory.port.PersistCapPort;

import java.util.Collection;
import java.util.Optional;

@Component
public class PersistCapAdapter implements PersistCapPort {
    private final CapEntityMapper capDomainMapper;
    private final CapRepository capRepository;
    private final CountryRepository countryRepository;

    public PersistCapAdapter(CapEntityMapper capDomainMapper, CapRepository capRepository, CountryRepository countryRepository) {
        this.capDomainMapper = capDomainMapper;
        this.capRepository = capRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public Collection<Cap> findAll() {
        var capEntities = capRepository.findAll();
        return capDomainMapper.toDomainObjects(capEntities);
    }

    @Override
    public Collection<Cap> findAllByCountry(String country) {
        var capEntities = capRepository.findAllByCountry_code(country);
        return capDomainMapper.toDomainObjects(capEntities);
    }

    @Override
    public Optional<Cap> findById(String businessId) {
        return capRepository.findByBusinessId(businessId)
            .map(capDomainMapper::toDomainObject);
    }

    @Override
    public void upsert(Cap cap) {
        var entity = capDomainMapper.toEntity(cap);
        capRepository.findByBusinessId(cap.businessId()).ifPresent(foundEntity -> entity.setId(foundEntity.getId()));
        countryRepository.findByCode(cap.country().code()).ifPresentOrElse(entity::setCountry, () -> countryRepository.save(entity.getCountry()));
        capRepository.save(entity);
    }

    @Override
    public void delete(Cap cap) {
        capRepository.delete(capRepository.findByBusinessId(cap.businessId()).orElseThrow());
    }
}
