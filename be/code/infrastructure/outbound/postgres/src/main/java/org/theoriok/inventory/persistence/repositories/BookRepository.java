package org.theoriok.inventory.persistence.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.theoriok.inventory.persistence.entities.BookEntity;

import java.util.UUID;

public interface BookRepository extends ListCrudRepository<BookEntity, UUID> {
}
