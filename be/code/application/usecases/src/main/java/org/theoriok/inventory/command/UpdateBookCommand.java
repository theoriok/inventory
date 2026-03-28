package org.theoriok.inventory.command;

import static org.theoriok.inventory.command.UpdateBook.Result.NOT_FOUND;
import static org.theoriok.inventory.command.UpdateBook.Result.UPDATED;

import org.theoriok.inventory.port.PersistBookPort;

@Command
public class UpdateBookCommand implements UpdateBook {

    private final PersistBookPort persistBookPort;

    public UpdateBookCommand(PersistBookPort persistBookPort) {
        this.persistBookPort = persistBookPort;
    }

    @Override
    public Result update(Request request) {
        return persistBookPort.findById(request.id())
            .map(book -> {
                persistBookPort.update(book.update(request.title(), request.author(), request.description()));
                return UPDATED;
            })
            .orElse(NOT_FOUND);
    }
}
