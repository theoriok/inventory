package org.theoriok.inventory.query;

import org.theoriok.inventory.CapId;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.port.PersistCapPort;

import java.util.List;
import java.util.Optional;

@Query
public class FindCapsQuery implements FindCaps {
    private final PersistCapPort persistCapPort;

    public FindCapsQuery(PersistCapPort persistCapPort) {
        this.persistCapPort = persistCapPort;
    }

    @Override
    public List<Cap> findAll(Request request) {
        return Optional.ofNullable(request.country())
            .map(persistCapPort::findAllByCountry)
            .orElseGet(persistCapPort::findAll);
    }

    @Override
    public Optional<Cap> findById(CapId id) {
        return persistCapPort.findById(id);
    }
}
