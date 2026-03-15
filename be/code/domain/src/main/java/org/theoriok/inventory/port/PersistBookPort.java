package org.theoriok.inventory.port;

import org.theoriok.inventory.BookId;
import org.theoriok.inventory.domain.Book;

import java.util.List;
import java.util.Optional;

public interface PersistBookPort {
    List<Book> findAll();

    Optional<Book> findById(BookId id);

    Book create(Book book);

    void update(Book book);

    boolean delete(BookId id);
}
