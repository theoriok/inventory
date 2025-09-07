import {renderHook, waitFor} from '@testing-library/react';
import {QueryClient, QueryClientProvider} from '@tanstack/react-query';
import {describe, expect, it, vi} from 'vitest';
import {ReactNode} from 'react';

import {useBook, useBooks} from './book.hook';
import {bookApi} from '../api/book.api';
import {generateBook} from '../__test__/generators/book.generator';

const createWrapper = () => {
    const queryClient = new QueryClient({
        defaultOptions: {
            queries: {retry: false},
        },
    });

    return function Wrapper({children}: { children: ReactNode }) {
        return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
    };
};

describe('useBooks', () => {
    it('should fetch books successfully', async () => {
        const books = [generateBook(), generateBook()];
        vi.spyOn(bookApi, 'fetchBooks').mockResolvedValue({
            items: books,
            total: books.length,
        });

        const {result} = renderHook(() => useBooks(), {
            wrapper: createWrapper(),
        });

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });

        expect(result.current.data?.items).toEqual(books);
        expect(result.current.data?.total).toBe(2);
        expect(bookApi.fetchBooks).toHaveBeenCalledOnce();
    });

    it('should handle fetch error', async () => {
        vi.spyOn(bookApi, 'fetchBooks').mockRejectedValue(new Error('API Error'));

        const {result} = renderHook(() => useBooks(), {
            wrapper: createWrapper(),
        });

        await waitFor(() => {
            expect(result.current.isError).toBe(true);
        });

        expect(result.current.error).toBeInstanceOf(Error);
    });

    it('should start in loading state', () => {
         const {result} = renderHook(() => useBooks(), {
            wrapper: createWrapper(),
        });

        expect(result.current.isLoading).toBe(true);
        expect(result.current.data).toBeUndefined();
    });
});

describe('useBook', () => {
  it('should fetch single book successfully', async () => {
    const book = generateBook();
    vi.spyOn(bookApi, 'fetchBook').mockResolvedValue(book);

    const { result } = renderHook(() => useBook('123'), {
      wrapper: createWrapper(),
    });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });

    expect(result.current.data).toEqual(book);
    expect(bookApi.fetchBook).toHaveBeenCalledWith('123');
  });

  it('should handle fetch error', async () => {
    vi.spyOn(bookApi, 'fetchBook').mockRejectedValue(new Error('Book not found'));

    const { result } = renderHook(() => useBook('invalid-id'), {
      wrapper: createWrapper(),
    });

    await waitFor(() => {
      expect(result.current.isError).toBe(true);
    });

    expect(result.current.error).toBeInstanceOf(Error);
  });

  it('should start in loading state', () => {
    const { result } = renderHook(() => useBook('123'), {
      wrapper: createWrapper(),
    });

    expect(result.current.isLoading).toBe(true);
    expect(result.current.data).toBeUndefined();
  });
});
