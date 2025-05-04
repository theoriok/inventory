package org.theoriok.inventory.query;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.port.PersistBookPort;

import java.util.List;
import java.util.Optional;

@Component
public class FindBooksQuery implements FindBooks {
    private final PersistBookPort persistBookPort;

    public FindBooksQuery(PersistBookPort persistBookPort) {
        this.persistBookPort = persistBookPort;
    }

    @Override
    public List<Book> findAll() {
        return persistBookPort.findAll().stream()
            .map(this::toResponseBook)
            .toList();
    }

    @Override
    public Optional<Book> findById(BookId businessId) {
        return persistBookPort.findById(businessId).map(this::toResponseBook);
    }

    private FindBooks.Book toResponseBook(org.theoriok.inventory.domain.Book book) {
        return new FindBooks.Book(book.businessId(), book.title(), book.author(), book.description());
    }
}
