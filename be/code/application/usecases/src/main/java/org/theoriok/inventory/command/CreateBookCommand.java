package org.theoriok.inventory.command;

import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.port.PersistBookPort;

@Command
public class CreateBookCommand implements CreateBook {

    private final PersistBookPort persistBookPort;

    public CreateBookCommand(PersistBookPort persistBookPort) {
        this.persistBookPort = persistBookPort;
    }

    @Override
    public Book create(Request request) {
        var book = Book.create(request.id(), request.title(), request.author(), request.description());
        return persistBookPort.create(book);
    }
}
