package org.theoriok.inventory.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.theoriok.inventory.persistence.entities.BookEntity;
import org.theoriok.inventory.persistence.entities.CapEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, UUID> {

}
