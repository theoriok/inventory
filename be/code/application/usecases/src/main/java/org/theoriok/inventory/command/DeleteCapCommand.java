package org.theoriok.inventory.command;

import static org.theoriok.inventory.command.DeleteCap.Result.DELETED;
import static org.theoriok.inventory.command.DeleteCap.Result.NOT_FOUND;

import org.theoriok.inventory.CapId;
import org.theoriok.inventory.port.PersistCapPort;

@Command
public class DeleteCapCommand implements DeleteCap {
    private final PersistCapPort persistCapPort;

    public DeleteCapCommand(PersistCapPort persistCapPort) {
        this.persistCapPort = persistCapPort;
    }

    @Override
    public Result delete(CapId id) {
        return persistCapPort.delete(id) ? DELETED : NOT_FOUND;
    }
}
