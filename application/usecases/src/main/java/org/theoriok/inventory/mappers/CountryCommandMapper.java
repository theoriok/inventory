package org.theoriok.inventory.mappers;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.query.FindCountries;

import java.util.Collection;

@Component
public class CountryCommandMapper implements CommandMapper<Country, Object, FindCountries.ListResponse, FindCountries.SingleResponse> {

    @Override
    public FindCountries.ListResponse toListResponse(Collection<Country> domainObjects) {
        return new FindCountries.ListResponse(domainObjects.stream().map(this::toResponseCountry).toList());
    }

    @Override
    public FindCountries.SingleResponse toSingleResponse(Country domainObject) {
        return new FindCountries.SingleResponse(toResponseCountry(domainObject));
    }

    @Override
    public Country toDomainObject(Object request) {
        throw new UnsupportedOperationException();
    }

    private FindCountries.Country toResponseCountry(Country country) {
        return new FindCountries.Country(
            country.name(),
            country.code()
        );
    }

}
