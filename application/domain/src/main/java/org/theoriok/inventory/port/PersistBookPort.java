package org.theoriok.inventory.port;

import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.domain.Cap;

import java.util.Collection;
import java.util.Optional;

public interface PersistBookPort {
    Collection<Book> findAll();
    Optional<Book> findById(String businessId);

    void upsert(Book book);
}
