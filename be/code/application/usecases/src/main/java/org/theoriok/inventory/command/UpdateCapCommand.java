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
        return persistCapPort.findById(request.id())
            .map(cap -> persistCountryPort.findByCode(request.country())
                .map(country -> {
                    persistCapPort.update(cap.update(request.name(), request.description(), request.amount(), country));
                    return UPDATED;
                })
                .orElse(UNKNOWN_COUNTRY)
            )
            .orElse(NOT_FOUND);
    }
}
