package org.theoriok.inventory.command;

import org.theoriok.inventory.BookId;
import org.theoriok.inventory.port.PersistBookPort;

@Command
public class DeleteBookCommand implements DeleteBook {
    private final PersistBookPort persistBookPort;

    public DeleteBookCommand(PersistBookPort persistBookPort) {
        this.persistBookPort = persistBookPort;
    }

    @Override
    public Result delete(BookId id) {
        return persistBookPort.delete(id) ? new Result.Deleted() : new Result.NotFound(id);
    }
}
