package org.theoriok.inventory.command;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.port.PersistCapPort;

@Component
public class DeleteCapCommand implements DeleteCap {
    private final PersistCapPort persistCapPort;

    public DeleteCapCommand(PersistCapPort persistCapPort) {
        this.persistCapPort = persistCapPort;
    }

    @Override
    public void delete(String businessId) {
        persistCapPort.delete(businessId);
    }
}
