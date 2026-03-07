package org.theoriok.inventory.command;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.port.PersistBookPort;

@Component
public class UpdateBookCommand implements UpdateBook {

    private final PersistBookPort persistBookPort;

    public UpdateBookCommand(PersistBookPort persistBookPort) {
        this.persistBookPort = persistBookPort;
    }

    @Override
    public Result update(Request request) {
        var book = persistBookPort.findById(request.id());

        if (book.isPresent()) {
            Book updatedBook = book.get().update(request.title(), request.author(), request.description());
            persistBookPort.update(updatedBook);
            return Result.UPDATED;
        } else {
            return Result.NOT_FOUND;
        }
    }
}
