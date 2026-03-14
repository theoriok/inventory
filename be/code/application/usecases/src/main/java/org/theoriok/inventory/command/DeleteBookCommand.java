package org.theoriok.inventory.command;

import static org.theoriok.inventory.command.DeleteBook.Result.DELETED;
import static org.theoriok.inventory.command.DeleteBook.Result.NOT_FOUND;

import org.theoriok.inventory.BookId;
import org.theoriok.inventory.port.PersistBookPort;

@Command
public class DeleteBookCommand implements DeleteBook {
    private final PersistBookPort persistBookPort;

    public DeleteBookCommand(PersistBookPort persistBookPort) {
        this.persistBookPort = persistBookPort;
    }

    @Override
    public Result delete(BookId businessId) {
        return persistBookPort.delete(businessId) ? DELETED : NOT_FOUND;
    }
}
