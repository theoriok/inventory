package org.theoriok.inventory.mappers;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.command.UpsertCap;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.domain.CapBuilder;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.query.FindCaps;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CapCommandMapper implements CommandMapper<Cap, UpsertCap.Request, FindCaps.ListResponse, FindCaps.SingleResponse> {

    @Override
    public FindCaps.ListResponse toListResponse(Collection<Cap> domainObjects) {
        return new FindCaps.ListResponse(domainObjects.stream().map(this::toResponseCap).collect(Collectors.toList()));
    }

    @Override
    public FindCaps.SingleResponse toSingleResponse(Cap domainObject) {
        return new FindCaps.SingleResponse(toResponseCap(domainObject));
    }

    @Override
    public Cap toDomainObject(UpsertCap.Request request) {
        return CapBuilder.builder()
            .businessId(request.businessId())
            .name(request.name())
            .description(request.description())
            .amount(request.amount())
            .build();
    }

    private FindCaps.Cap toResponseCap(Cap cap) {
        return new FindCaps.Cap(
            cap.businessId(),
            cap.name(),
            cap.description(),
            cap.amount(),
            toResponseCountry(cap.country())
        );
    }

    private FindCaps.Country toResponseCountry(Country country) {
        return new FindCaps.Country(
            country.name(),
            country.code()
        );
    }

}
