package org.theoriok.inventory.persistence.repositories;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.theoriok.inventory.persistence.entities.BookEntity;

import java.util.UUID;

public interface BookRepository extends ListCrudRepository<BookEntity, UUID> {
    @Modifying
    @Query("DELETE FROM book WHERE id = :id")
    int deleteByIdReturningCount(@Param("id") UUID id);

}
