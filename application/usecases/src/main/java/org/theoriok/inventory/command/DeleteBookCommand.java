package org.theoriok.inventory.command;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.port.PersistBookPort;

@Component
public class DeleteBookCommand implements DeleteBook {
    private final PersistBookPort persistBookPort;

    public DeleteBookCommand(PersistBookPort persistBookPort) {
        this.persistBookPort = persistBookPort;
    }

    @Override
    public void delete(String businessId) {
        persistBookPort.delete(businessId);
    }
}
