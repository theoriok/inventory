package org.theoriok.inventory.query;

import org.theoriok.inventory.BookId;

import java.util.List;
import java.util.Optional;

public interface FindBooks {
    ListResponse findAll();

    Optional<SingleResponse> findById(String id);

    record ListResponse(List<Book> books) {
    }

    record SingleResponse(Book book) {
    }

    record Book(BookId businessId, String title, String author, String description) {
    }
}
