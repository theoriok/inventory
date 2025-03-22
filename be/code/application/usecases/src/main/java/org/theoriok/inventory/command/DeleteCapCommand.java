package org.theoriok.inventory.command;

import static org.theoriok.inventory.command.DeleteCap.Result.DELETED;
import static org.theoriok.inventory.command.DeleteCap.Result.NOT_FOUND;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.port.PersistCapPort;

@Component
public class DeleteCapCommand implements DeleteCap {
    private final PersistCapPort persistCapPort;

    public DeleteCapCommand(PersistCapPort persistCapPort) {
        this.persistCapPort = persistCapPort;
    }

    @Override
    public Result delete(String businessId) {
        return persistCapPort.findById(businessId)
            .map(cap -> {
                persistCapPort.delete(cap);
                    return DELETED;
                }
            )
            .orElse(NOT_FOUND);
    }
}
