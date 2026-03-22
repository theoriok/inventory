import {useMutation, useQuery, useQueryClient, UseQueryResult,} from '@tanstack/react-query';

import {bookApi} from '../api/book.api.ts';
import {Book, CreateBook, ListResponse, UpdateBook,} from '../api/book.api.types.ts';

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

export function useCreateBook() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (book: CreateBook) => bookApi.createBook(book),
        onSuccess: () => queryClient.invalidateQueries({queryKey: [BookQueryKeys.Books]}),
    });
}

export function useUpdateBook() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: ({id, book}: { id: string; book: UpdateBook }) => bookApi.updateBook(id, book),
        onSuccess: () => queryClient.invalidateQueries({queryKey: [BookQueryKeys.Books]}),
    });
}

export function useDeleteBook() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => bookApi.deleteBook(id),
        onSuccess: () => queryClient.invalidateQueries({queryKey: [BookQueryKeys.Books]}),
    });
}

