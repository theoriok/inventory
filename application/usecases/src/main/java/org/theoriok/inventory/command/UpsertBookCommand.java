package org.theoriok.inventory.command;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.mappers.BookCommandMapper;
import org.theoriok.inventory.port.PersistBookPort;

@Component
public class UpsertBookCommand implements UpsertBook {

    private final PersistBookPort persistBookPort;
    private final BookCommandMapper bookCommandMapper;

    public UpsertBookCommand(PersistBookPort persistBookPort, BookCommandMapper bookCommandMapper) {
        this.persistBookPort = persistBookPort;
        this.bookCommandMapper = bookCommandMapper;
    }

    @Override
    public void upsert(Request request) {
        persistBookPort.upsert(bookCommandMapper.toDomainObject(request));
    }
}
