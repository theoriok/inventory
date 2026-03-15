package org.theoriok.inventory.persistence.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.theoriok.inventory.persistence.entities.CountryEntity;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends ListCrudRepository<CountryEntity, UUID> {
    Optional<CountryEntity> findByCode(String code);
}
