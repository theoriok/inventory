package org.theoriok.inventory.query;

import org.theoriok.inventory.BookId;

import java.util.List;
import java.util.Optional;

public interface FindBooks {
    List<Book> findAll();

    Optional<Book> findById(BookId id);

    record Book(BookId businessId, String title, String author, String description) {
    }
}
