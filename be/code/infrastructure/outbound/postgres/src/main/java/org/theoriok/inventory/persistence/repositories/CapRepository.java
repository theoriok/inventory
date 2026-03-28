package org.theoriok.inventory.persistence.repositories;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.theoriok.inventory.persistence.entities.CapEntity;

import java.util.List;
import java.util.UUID;

public interface CapRepository extends ListCrudRepository<CapEntity, UUID> {
    List<CapEntity> findAllByCountryCode(String countryCode);

    @Modifying
    @Query("DELETE FROM cap WHERE id = :id")
    int deleteByIdReturningCount(@Param("id") UUID id);
}
