package org.theoriok.inventory.query;

import org.theoriok.inventory.BookId;
import org.theoriok.inventory.domain.Book;

import java.util.List;
import java.util.Optional;

public interface FindBooks {
    List<Book> findAll();

    Optional<Book> findById(BookId id);

}
