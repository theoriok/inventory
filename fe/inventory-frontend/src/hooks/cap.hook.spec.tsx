import {renderHook, waitFor} from '@testing-library/react';
import {describe, expect, it, vi} from 'vitest';

import {useCap, useCaps, useCreateCap, useDeleteCap, useUpdateCap} from './cap.hook';
import {capApi} from '../api/cap.api';
import {generateCap, generateCreateCap, generateUpdateCap} from '../__test__/generators/cap.generator';
import {createWrapper} from '../__test__/helpers/hook.helper';

describe('useCaps', () => {
    it('should fetch caps successfully', async () => {
        const caps = [generateCap(), generateCap()];
        vi.spyOn(capApi, 'fetchCaps').mockResolvedValue({
            items: caps,
            total: caps.length,
        });

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCaps(), {wrapper});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(result.current.data?.items).toEqual(caps);
        expect(result.current.data?.total).toBe(2);
        expect(capApi.fetchCaps).toHaveBeenCalledOnce();
    });

    it('should handle fetch error', async () => {
        vi.spyOn(capApi, 'fetchCaps').mockRejectedValue(new Error('API Error'));

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCaps(), {wrapper});

        await waitFor(() => {
            expect(result.current.isError).toBe(true);
        });

        expect(result.current.error).toBeInstanceOf(Error);
    });

    it('should start in loading state', () => {
        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCaps(), {wrapper});

        expect(result.current.isLoading).toBe(true);
        expect(result.current.data).toBeUndefined();
    });
});

describe('useCap', () => {
    it('should fetch single cap successfully', async () => {
        const cap = generateCap();
        vi.spyOn(capApi, 'fetchCap').mockResolvedValue(cap);

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCap('123'), {wrapper});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(result.current.data).toEqual(cap);
        expect(capApi.fetchCap).toHaveBeenCalledWith('123');
    });

    it('should handle fetch error', async () => {
        vi.spyOn(capApi, 'fetchCap').mockRejectedValue(new Error('Cap not found'));

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCap('invalid-id'), {wrapper});

        await waitFor(() => {
            expect(result.current.isError).toBe(true);
        });

        expect(result.current.error).toBeInstanceOf(Error);
    });

    it('should start in loading state', () => {
        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCap('123'), {wrapper});

        expect(result.current.isLoading).toBe(true);
        expect(result.current.data).toBeUndefined();
    });
});

describe('useCreateCap', () => {
    it('should call createCap and return created cap', async () => {
        const createCap = generateCreateCap();
        const createdCap = generateCap();
        vi.spyOn(capApi, 'createCap').mockResolvedValue(createdCap);

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCreateCap(), {wrapper});

        result.current.mutate(createCap);

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(result.current.data).toEqual(createdCap);
        expect(capApi.createCap).toHaveBeenCalledWith(createCap);
    });

    it('should invalidate caps query on success', async () => {
        const {wrapper, queryClient} = createWrapper();
        const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries');
        vi.spyOn(capApi, 'createCap').mockResolvedValue(generateCap());

        const {result} = renderHook(() => useCreateCap(), {wrapper});

        result.current.mutate(generateCreateCap());

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(invalidateSpy).toHaveBeenCalledWith({queryKey: ['caps']});
    });
});

describe('useUpdateCap', () => {
    it('should call updateCap with id and body', async () => {
        const updateCap = generateUpdateCap();
        vi.spyOn(capApi, 'updateCap').mockResolvedValue(undefined);

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useUpdateCap(), {wrapper});

        result.current.mutate({id: '456', cap: updateCap});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(capApi.updateCap).toHaveBeenCalledWith('456', updateCap);
    });

    it('should invalidate caps query on success', async () => {
        const {wrapper, queryClient} = createWrapper();
        const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries');
        vi.spyOn(capApi, 'updateCap').mockResolvedValue(undefined);

        const {result} = renderHook(() => useUpdateCap(), {wrapper});

        result.current.mutate({id: '456', cap: generateUpdateCap()});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(invalidateSpy).toHaveBeenCalledWith({queryKey: ['caps']});
    });
});

describe('useDeleteCap', () => {
    it('should call deleteCap with id', async () => {
        vi.spyOn(capApi, 'deleteCap').mockResolvedValue(undefined);

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useDeleteCap(), {wrapper});

        result.current.mutate('789');

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(capApi.deleteCap).toHaveBeenCalledWith('789');
    });

    it('should invalidate caps query on success', async () => {
        const {wrapper, queryClient} = createWrapper();
        const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries');
        vi.spyOn(capApi, 'deleteCap').mockResolvedValue(undefined);

        const {result} = renderHook(() => useDeleteCap(), {wrapper});

        result.current.mutate('789');

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(invalidateSpy).toHaveBeenCalledWith({queryKey: ['caps']});
    });
});
