import {useMutation, useQuery, useQueryClient, UseQueryResult,} from '@tanstack/react-query';

import {bookApi} from '../api/book.api.ts';
import {Book, CreateBook, UpdateBook,} from '../api/book.api.types.ts';
import {ListResponse, ProblemDetailError} from '../api/api.types.ts';

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

export function useCreateBook({onSuccess, onValidationError, onDetailedError, onError}: {
    onSuccess?: () => void;
    onValidationError?: (errors: Record<string, string>) => void;
    onDetailedError?: (detail: string) => void;
    onError?: () => void;
} = {}) {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (book: CreateBook) => bookApi.createBook(book),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: [BookQueryKeys.Books]});
            onSuccess?.();
        },
        onError: (error) => {
            if (error instanceof ProblemDetailError) {
                if (error.problemDetail.errors) {
                    onValidationError?.(error.problemDetail.errors);
                } else {
                    onDetailedError?.(error.problemDetail.detail);
                }
            } else {
                onError?.();
            }
        },
    });
}

export function useUpdateBook() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: ({id, book}: { id: string; book: UpdateBook }) => bookApi.updateBook(id, book),
        onSuccess: () => queryClient.invalidateQueries({queryKey: [BookQueryKeys.Books]}),
    });
}

export function useDeleteBook({onSuccess, onError}: {
    onSuccess?: () => void;
    onError?: (detail: string) => void;
} = {}) {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => bookApi.deleteBook(id),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: [BookQueryKeys.Books]});
            onSuccess?.();
        },
        onError: (error) => {
            if (error instanceof ProblemDetailError) {
                onError?.(error.problemDetail.detail);
            } else {
                onError?.('something went wrong');
            }
        },
    });
}

