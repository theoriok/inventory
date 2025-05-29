package org.theoriok.inventory.query;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.port.PersistCapPort;

import java.util.List;
import java.util.Optional;

@Component
public class FindCapsQuery implements FindCaps {
    private final PersistCapPort persistCapPort;

    public FindCapsQuery(PersistCapPort persistCapPort) {
        this.persistCapPort = persistCapPort;
    }

    @Override
    public List<Cap> findAll(Request request) {
        var caps = Optional.ofNullable(request.country())
            .map(persistCapPort::findAllByCountry)
            .orElseGet(persistCapPort::findAll);
        return caps.stream().toList();
    }

    @Override
    public Optional<Cap> findById(String businessId) {
        return persistCapPort.findById(businessId);
    }
}
