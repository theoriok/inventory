package org.theoriok.inventory.query;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.mappers.CapCommandMapper;
import org.theoriok.inventory.port.PersistCapPort;

import java.util.Optional;

@Component
public class FindCapsQuery implements FindCaps {
    private final PersistCapPort persistCapPort;
    private final CapCommandMapper capCommandMapper;

    public FindCapsQuery(PersistCapPort persistCapPort, CapCommandMapper capCommandMapper) {
        this.persistCapPort = persistCapPort;
        this.capCommandMapper = capCommandMapper;
    }

    @Override
    public ListResponse findAll(Request request) {
        var caps = Optional.ofNullable(request.country())
            .map(persistCapPort::findAllByCountry)
            .orElseGet(persistCapPort::findAll);
        return capCommandMapper.toListResponse(caps);
    }

    @Override
    public Optional<SingleResponse> findById(String businessId) {
        return persistCapPort.findById(businessId).map(capCommandMapper::toSingleResponse);
    }
}
