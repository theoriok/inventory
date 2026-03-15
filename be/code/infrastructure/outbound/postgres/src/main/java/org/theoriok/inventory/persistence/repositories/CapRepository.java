package org.theoriok.inventory.persistence.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.theoriok.inventory.persistence.entities.CapEntity;

import java.util.Collection;
import java.util.UUID;

public interface CapRepository extends ListCrudRepository<CapEntity, UUID> {
    Collection<CapEntity> findAllByCountryCode(String countryCode);
}
