import {useQuery, UseQueryResult} from '@tanstack/react-query';

import {countryApi} from '../api/country.api.ts';
import {Country} from '../api/country.api.types.ts';
import {ListResponse} from '../api/api.types.ts';

export enum CountryQueryKeys {
    Countries = 'countries',
    Country = 'country',
}

export function useCountries(): UseQueryResult<ListResponse<Country>> {
    return useQuery({
        queryKey: [CountryQueryKeys.Countries],
        queryFn: () => countryApi.fetchCountries(),
    });
}

export function useCountry(code: string): UseQueryResult<Country> {
    return useQuery({
        queryKey: [CountryQueryKeys.Country, code],
        queryFn: () => countryApi.fetchCountry(code),
    });
}
