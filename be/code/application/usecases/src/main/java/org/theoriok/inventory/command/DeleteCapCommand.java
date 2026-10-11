package org.theoriok.inventory.command;

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
        return persistCapPort.delete(id) ? new Result.Deleted() : new Result.NotFound(id);
    }
}
