package org.theoriok.inventory.query;

import org.theoriok.inventory.BookId;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.port.PersistBookPort;

import java.util.List;
import java.util.Optional;

@Query
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
    public Optional<Book> findById(BookId id) {
        return persistBookPort.findById(id);
    }

}
