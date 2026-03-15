package org.theoriok.inventory.persistence.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.theoriok.inventory.persistence.entities.CapEntity;

import java.util.List;
import java.util.UUID;

public interface CapRepository extends ListCrudRepository<CapEntity, UUID> {
    List<CapEntity> findAllByCountryCode(String countryCode);
}
