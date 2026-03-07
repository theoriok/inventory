package org.theoriok.inventory.port;

import org.theoriok.inventory.BookId;
import org.theoriok.inventory.domain.Book;

import java.util.Collection;
import java.util.Optional;

public interface PersistBookPort {
    Collection<Book> findAll();

    Optional<Book> findById(BookId id);

    Book create(Book book);

    boolean update(Book book);

    boolean delete(BookId id);
}
