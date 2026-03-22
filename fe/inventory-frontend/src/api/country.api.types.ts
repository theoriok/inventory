import {ListResponse} from './api.types.ts';

export interface CountryApi {
    fetchCountries(): Promise<ListResponse<Country>>;

    fetchCountry(code: string): Promise<Country>;
}

export interface Country {
    name: string;
    code: string;
}
