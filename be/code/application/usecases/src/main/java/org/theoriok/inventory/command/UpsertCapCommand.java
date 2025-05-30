package org.theoriok.inventory.command;

import static org.theoriok.inventory.command.UpsertCap.Result.UNKNOWN_COUNTRY;
import static org.theoriok.inventory.command.UpsertCap.Result.UPSERTED;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.port.PersistCapPort;
import org.theoriok.inventory.port.PersistCountryPort;

@Component
public class UpsertCapCommand implements UpsertCap {

    private final PersistCapPort persistCapPort;
    private final PersistCountryPort persistCountryPort;

    public UpsertCapCommand(PersistCapPort persistCapPort, PersistCountryPort persistCountryPort) {
        this.persistCapPort = persistCapPort;
        this.persistCountryPort = persistCountryPort;
    }

    @Override
    public Result upsert(Request request) {
        return persistCountryPort.findByCode(request.country())
            .map(country -> {
                var cap = Cap.create(request.businessId(), request.name(), request.description(), request.amount(), country);
                persistCapPort.upsert(cap);
                return UPSERTED;
            })
            .orElse(UNKNOWN_COUNTRY);
    }
}
