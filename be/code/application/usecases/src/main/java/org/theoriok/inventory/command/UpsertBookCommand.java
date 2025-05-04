package org.theoriok.inventory.command;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.port.PersistBookPort;

@Component
public class UpsertBookCommand implements UpsertBook {

    private final PersistBookPort persistBookPort;

    public UpsertBookCommand(PersistBookPort persistBookPort) {
        this.persistBookPort = persistBookPort;
    }

    @Override
    public void upsert(Request request) {
        var book = Book.create(request.businessId(), request.author(), request.title(), request.description());
        persistBookPort.upsert(book);
    }
}
