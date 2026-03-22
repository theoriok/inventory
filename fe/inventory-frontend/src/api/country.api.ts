import {Country, CountryApi} from "./country.api.types.ts";
import {ListResponse} from "./api.types.ts";
import {baseApi} from './base.api.ts';
import {AxiosResponse} from "axios";

export const countryApi: CountryApi = {
    async fetchCountries(): Promise<ListResponse<Country>> {
        const {data: countries}: AxiosResponse<Country[]> = await baseApi.get('/countries');
        return {items: countries, total: countries.length};
    },

    async fetchCountry(code: string): Promise<Country> {
        const {data: country}: AxiosResponse<Country> = await baseApi.get(`/countries/${code}`);
        return country;
    }
}
