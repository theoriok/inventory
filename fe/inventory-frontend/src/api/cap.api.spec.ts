import {describe, expect, test, vi} from 'vitest';

import {baseApi} from './base.api.ts';
import {capApi} from './cap.api.ts';
import {generateCap, generateCreateCap, generateUpdateCap} from '../__test__/generators/cap.generator.ts';

describe('CapApi', () => {
    describe('fetch caps', () => {
        test('should get /caps', async () => {
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: [generateCap()]});

            await capApi.fetchCaps();

            expect(baseApi.get).toHaveBeenCalledWith('/caps');
        });

        test('returns the caps', async () => {
            const caps = [generateCap()];
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: caps});

            const result = await capApi.fetchCaps();

            expect(result).toEqual({items: caps, total: 1});
        });
    });

    describe('fetch cap by id', () => {
        test('should get /caps/{id}', async () => {
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: generateCap()});

            await capApi.fetchCap('123');

            expect(baseApi.get).toHaveBeenCalledWith('/caps/123');
        });

        test('returns a single cap', async () => {
            const cap = generateCap();
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: cap});

            const result = await capApi.fetchCap('123');

            expect(result).toEqual(cap);
        });
    });

    describe('create cap', () => {
        test('should post to /caps', async () => {
            const createCap = generateCreateCap();
            const createdCap = generateCap();
            vi.spyOn(baseApi, 'post').mockResolvedValue({data: createdCap});

            await capApi.createCap(createCap);

            expect(baseApi.post).toHaveBeenCalledWith('/caps', createCap);
        });

        test('returns the created cap', async () => {
            const createCap = generateCreateCap();
            const createdCap = generateCap();
            vi.spyOn(baseApi, 'post').mockResolvedValue({data: createdCap});

            const result = await capApi.createCap(createCap);

            expect(result).toEqual(createdCap);
        });
    });

    describe('update cap', () => {
        test('should put to /caps/{id}', async () => {
            const updateCap = generateUpdateCap();
            vi.spyOn(baseApi, 'put').mockResolvedValue({data: undefined});

            await capApi.updateCap('456', updateCap);

            expect(baseApi.put).toHaveBeenCalledWith('/caps/456', updateCap);
        });

        test('returns void', async () => {
            const updateCap = generateUpdateCap();
            vi.spyOn(baseApi, 'put').mockResolvedValue({data: undefined});

            const result = await capApi.updateCap('456', updateCap);

            expect(result).toBeUndefined();
        });
    });

    describe('delete cap', () => {
        test('should delete /caps/{id}', async () => {
            vi.spyOn(baseApi, 'delete').mockResolvedValue({data: undefined});

            await capApi.deleteCap('789');

            expect(baseApi.delete).toHaveBeenCalledWith('/caps/789');
        });

        test('returns void', async () => {
            vi.spyOn(baseApi, 'delete').mockResolvedValue({data: undefined});

            const result = await capApi.deleteCap('789');

            expect(result).toBeUndefined();
        });
    });
});
