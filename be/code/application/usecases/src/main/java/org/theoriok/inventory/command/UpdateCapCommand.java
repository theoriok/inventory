package org.theoriok.inventory.command;

import static org.theoriok.inventory.command.UpdateCap.Result.NOT_FOUND;
import static org.theoriok.inventory.command.UpdateCap.Result.UNKNOWN_COUNTRY;
import static org.theoriok.inventory.command.UpdateCap.Result.UPDATED;

import org.theoriok.inventory.port.PersistCapPort;
import org.theoriok.inventory.port.PersistCountryPort;

@Command
public class UpdateCapCommand implements UpdateCap {

    private final PersistCapPort persistCapPort;
    private final PersistCountryPort persistCountryPort;

    public UpdateCapCommand(PersistCapPort persistCapPort, PersistCountryPort persistCountryPort) {
        this.persistCapPort = persistCapPort;
        this.persistCountryPort = persistCountryPort;
    }

    @Override
    public Result update(Request request) {
        var existingCap = persistCapPort.findById(request.id());
        if (existingCap.isEmpty()) {
            return NOT_FOUND;
        }
        return persistCountryPort.findByCode(request.country())
            .map(country -> {
                var cap = existingCap.get().update(request.name(), request.description(), request.amount(), country);
                persistCapPort.update(cap);
                return UPDATED;
            })
            .orElse(UNKNOWN_COUNTRY);
    }
}
