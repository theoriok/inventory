package org.theoriok.inventory.command;

import static org.theoriok.inventory.command.DeleteBook.Result.DELETED;
import static org.theoriok.inventory.command.DeleteBook.Result.NOT_FOUND;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.port.PersistBookPort;

@Component
public class DeleteBookCommand implements DeleteBook {
    private final PersistBookPort persistBookPort;

    public DeleteBookCommand(PersistBookPort persistBookPort) {
        this.persistBookPort = persistBookPort;
    }

    @Override
    public Result delete(String businessId) {
        return persistBookPort.findById(businessId)
            .map(book -> {
                    persistBookPort.delete(book);
                    return DELETED;
                }
            )
            .orElse(NOT_FOUND);
    }
}
