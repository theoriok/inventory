import {describe, expect, test, vi} from 'vitest';

import {baseApi} from './base.api.ts';
import {countryApi} from './country.api.ts';
import {generateCountry} from '../__test__/generators/country.generator.ts';

describe('CountryApi', () => {
    describe('fetch countries', () => {
        test('should get /countries', async () => {
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: [generateCountry()]});

            await countryApi.fetchCountries();

            expect(baseApi.get).toHaveBeenCalledWith('/countries');
        });

        test('returns the countries', async () => {
            const countries = [generateCountry()];
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: countries});

            const result = await countryApi.fetchCountries();

            expect(result).toEqual({items: countries, total: 1});
        });
    });

    describe('fetch country by code', () => {
        test('should get /countries/{code}', async () => {
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: generateCountry()});

            await countryApi.fetchCountry('BE');

            expect(baseApi.get).toHaveBeenCalledWith('/countries/BE');
        });

        test('returns a single country', async () => {
            const country = generateCountry();
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: country});

            const result = await countryApi.fetchCountry('BE');

            expect(result).toEqual(country);
        });
    });
});
