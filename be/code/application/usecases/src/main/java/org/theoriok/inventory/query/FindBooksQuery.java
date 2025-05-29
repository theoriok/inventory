package org.theoriok.inventory.query;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.domain.Book;
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
        return persistBookPort.findAll().stream().toList();
    }

    @Override
    public Optional<Book> findById(BookId businessId) {
        return persistBookPort.findById(businessId);
    }

}
