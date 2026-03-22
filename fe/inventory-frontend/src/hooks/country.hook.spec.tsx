import {renderHook, waitFor} from '@testing-library/react';
import {describe, expect, it, vi} from 'vitest';

import {useCountries, useCountry} from './country.hook';
import {countryApi} from '../api/country.api';
import {generateCountry} from '../__test__/generators/country.generator';
import {createWrapper} from '../__test__/helpers/hook.helper';

describe('useCountries', () => {
    it('should fetch countries successfully', async () => {
        const countries = [generateCountry(), generateCountry()];
        vi.spyOn(countryApi, 'fetchCountries').mockResolvedValue({
            items: countries,
            total: countries.length,
        });

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCountries(), {wrapper});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(result.current.data?.items).toEqual(countries);
        expect(result.current.data?.total).toBe(2);
        expect(countryApi.fetchCountries).toHaveBeenCalledOnce();
    });

    it('should handle fetch error', async () => {
        vi.spyOn(countryApi, 'fetchCountries').mockRejectedValue(new Error('API Error'));

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCountries(), {wrapper});

        await waitFor(() => {
            expect(result.current.isError).toBe(true);
        });

        expect(result.current.error).toBeInstanceOf(Error);
    });

    it('should start in loading state', () => {
        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCountries(), {wrapper});

        expect(result.current.isLoading).toBe(true);
        expect(result.current.data).toBeUndefined();
    });
});

describe('useCountry', () => {
    it('should fetch single country successfully', async () => {
        const country = generateCountry();
        vi.spyOn(countryApi, 'fetchCountry').mockResolvedValue(country);

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCountry('BE'), {wrapper});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(result.current.data).toEqual(country);
        expect(countryApi.fetchCountry).toHaveBeenCalledWith('BE');
    });

    it('should handle fetch error', async () => {
        vi.spyOn(countryApi, 'fetchCountry').mockRejectedValue(new Error('Country not found'));

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCountry('XX'), {wrapper});

        await waitFor(() => {
            expect(result.current.isError).toBe(true);
        });

        expect(result.current.error).toBeInstanceOf(Error);
    });

    it('should start in loading state', () => {
        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCountry('BE'), {wrapper});

        expect(result.current.isLoading).toBe(true);
        expect(result.current.data).toBeUndefined();
    });
});
