import {useQuery, UseQueryResult,} from '@tanstack/react-query';

import {bookApi} from '../api/book.api.ts';
import {Book, ListResponse,} from '../api/book.api.types.ts';

export enum BookQueryKeys {
    Books = 'books',
    Book = 'book',
}

export function useBooks(): UseQueryResult<ListResponse<Book>> {
    return useQuery({
        queryKey: [BookQueryKeys.Books],
        queryFn: () => bookApi.fetchBooks(),
    });
}

export function useBook(id: string): UseQueryResult<Book> {
    return useQuery({
        queryKey: [BookQueryKeys.Book, id],
        queryFn: () => bookApi.fetchBook(id),
    });
}

