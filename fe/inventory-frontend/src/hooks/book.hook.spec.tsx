import {renderHook, waitFor} from '@testing-library/react';
import {QueryClient, QueryClientProvider} from '@tanstack/react-query';
import {describe, expect, it, vi} from 'vitest';
import {ReactNode} from 'react';

import {useBook, useBooks, useCreateBook, useDeleteBook, useUpdateBook} from './book.hook';
import {bookApi} from '../api/book.api';
import {generateBook, generateCreateBook, generateUpdateBook} from '../__test__/generators/book.generator';

const createWrapper = () => {
    const queryClient = new QueryClient({
        defaultOptions: {
            queries: {retry: false},
        },
    });

    const wrapper = function Wrapper({children}: { children: ReactNode }) {
        return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
    };

    return {wrapper, queryClient};
};

describe('useBooks', () => {
    it('should fetch books successfully', async () => {
        const books = [generateBook(), generateBook()];
        vi.spyOn(bookApi, 'fetchBooks').mockResolvedValue({
            items: books,
            total: books.length,
        });

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useBooks(), {wrapper});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(result.current.data?.items).toEqual(books);
        expect(result.current.data?.total).toBe(2);
        expect(bookApi.fetchBooks).toHaveBeenCalledOnce();
    });

    it('should handle fetch error', async () => {
        vi.spyOn(bookApi, 'fetchBooks').mockRejectedValue(new Error('API Error'));

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useBooks(), {wrapper});

        await waitFor(() => {
            expect(result.current.isError).toBe(true);
        });

        expect(result.current.error).toBeInstanceOf(Error);
    });

    it('should start in loading state', () => {
        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useBooks(), {wrapper});

        expect(result.current.isLoading).toBe(true);
        expect(result.current.data).toBeUndefined();
    });
});

describe('useBook', () => {
    it('should fetch single book successfully', async () => {
        const book = generateBook();
        vi.spyOn(bookApi, 'fetchBook').mockResolvedValue(book);

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useBook('123'), {wrapper});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(result.current.data).toEqual(book);
        expect(bookApi.fetchBook).toHaveBeenCalledWith('123');
    });

    it('should handle fetch error', async () => {
        vi.spyOn(bookApi, 'fetchBook').mockRejectedValue(new Error('Book not found'));

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useBook('invalid-id'), {wrapper});

        await waitFor(() => {
            expect(result.current.isError).toBe(true);
        });

        expect(result.current.error).toBeInstanceOf(Error);
    });

    it('should start in loading state', () => {
        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useBook('123'), {wrapper});

        expect(result.current.isLoading).toBe(true);
        expect(result.current.data).toBeUndefined();
    });
});

describe('useCreateBook', () => {
    it('should call createBook and return created book', async () => {
        const createBook = generateCreateBook();
        const createdBook = generateBook();
        vi.spyOn(bookApi, 'createBook').mockResolvedValue(createdBook);

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useCreateBook(), {wrapper});

        result.current.mutate(createBook);

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(result.current.data).toEqual(createdBook);
        expect(bookApi.createBook).toHaveBeenCalledWith(createBook);
    });

    it('should invalidate books query on success', async () => {
        const {wrapper, queryClient} = createWrapper();
        const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries');
        vi.spyOn(bookApi, 'createBook').mockResolvedValue(generateBook());

        const {result} = renderHook(() => useCreateBook(), {wrapper});

        result.current.mutate(generateCreateBook());

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(invalidateSpy).toHaveBeenCalledWith({queryKey: ['books']});
    });
});

describe('useUpdateBook', () => {
    it('should call updateBook with id and body', async () => {
        const updateBook = generateUpdateBook();
        vi.spyOn(bookApi, 'updateBook').mockResolvedValue(undefined);

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useUpdateBook(), {wrapper});

        result.current.mutate({id: '456', book: updateBook});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(bookApi.updateBook).toHaveBeenCalledWith('456', updateBook);
    });

    it('should invalidate books query on success', async () => {
        const {wrapper, queryClient} = createWrapper();
        const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries');
        vi.spyOn(bookApi, 'updateBook').mockResolvedValue(undefined);

        const {result} = renderHook(() => useUpdateBook(), {wrapper});

        result.current.mutate({id: '456', book: generateUpdateBook()});

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(invalidateSpy).toHaveBeenCalledWith({queryKey: ['books']});
    });
});

describe('useDeleteBook', () => {
    it('should call deleteBook with id', async () => {
        vi.spyOn(bookApi, 'deleteBook').mockResolvedValue(undefined);

        const {wrapper} = createWrapper();
        const {result} = renderHook(() => useDeleteBook(), {wrapper});

        result.current.mutate('789');

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(bookApi.deleteBook).toHaveBeenCalledWith('789');
    });

    it('should invalidate books query on success', async () => {
        const {wrapper, queryClient} = createWrapper();
        const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries');
        vi.spyOn(bookApi, 'deleteBook').mockResolvedValue(undefined);

        const {result} = renderHook(() => useDeleteBook(), {wrapper});

        result.current.mutate('789');

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(invalidateSpy).toHaveBeenCalledWith({queryKey: ['books']});
    });
});
