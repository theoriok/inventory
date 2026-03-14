package org.theoriok.inventory.command;

import static org.theoriok.inventory.command.CreateCap.Result.CREATED;
import static org.theoriok.inventory.command.CreateCap.Result.UNKNOWN_COUNTRY;

import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.port.PersistCapPort;
import org.theoriok.inventory.port.PersistCountryPort;

@Command
public class CreateCapCommand implements CreateCap {

    private final PersistCapPort persistCapPort;
    private final PersistCountryPort persistCountryPort;

    public CreateCapCommand(PersistCapPort persistCapPort, PersistCountryPort persistCountryPort) {
        this.persistCapPort = persistCapPort;
        this.persistCountryPort = persistCountryPort;
    }

    @Override
    public Result create(Request request) {
        return persistCountryPort.findByCode(request.country())
            .map(country -> {
                var cap = Cap.create(request.id(), request.name(), request.description(), request.amount(), country);
                persistCapPort.create(cap);
                return CREATED;
            })
            .orElse(UNKNOWN_COUNTRY);
    }
}
