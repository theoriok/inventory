package org.theoriok.inventory.query;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.mappers.BookCommandMapper;
import org.theoriok.inventory.port.PersistBookPort;

@Component
public class FindBooksQuery implements FindBooks {
    private final PersistBookPort persistBookPort;
    private final BookCommandMapper bookCommandMapper;

    public FindBooksQuery(PersistBookPort persistBookPort, BookCommandMapper bookCommandMapper) {
        this.persistBookPort = persistBookPort;
        this.bookCommandMapper = bookCommandMapper;
    }

    @Override
    public ListResponse findAll() {
        return bookCommandMapper.toListResponse(persistBookPort.findAll());
    }
}
