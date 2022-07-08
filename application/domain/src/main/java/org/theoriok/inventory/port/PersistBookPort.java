package org.theoriok.inventory.port;

import org.theoriok.inventory.domain.Book;

import java.util.Collection;

public interface PersistBookPort {
    Collection<Book> findAll();

    void upsert(Book book);
}
