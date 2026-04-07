import {describe, expect, test, vi} from 'vitest';
import {AxiosError} from 'axios';

import {baseApi} from './base.api.ts';
import {bookApi} from './book.api.ts';
import {generateBook, generateCreateBook, generateUpdateBook} from '../__test__/generators/book.generator.ts';
import {ProblemDetailError} from './api.types.ts';

describe('BookApi', () => {
    describe('fetch books', () => {
        test('should fetch the user info', async () => {
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: [generateBook()]});

            await bookApi.fetchBooks();

            expect(baseApi.get).toHaveBeenCalledWith(`/books`);
        });

        test('returns the books', async () => {
            const books = [generateBook()];
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: books});

            const result = await bookApi.fetchBooks();

            expect(result).toEqual({items: books, total: 1});
        });
    });

    describe('fetch book by id', () => {
        test('should fetch the user info', async () => {
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: generateBook()});

            await bookApi.fetchBook('123');

            expect(baseApi.get).toHaveBeenCalledWith(`/books/123`);
        });

        test('returns a single book', async () => {
            const book = generateBook();
            vi.spyOn(baseApi, 'get').mockResolvedValue({data: book});

            const result = await bookApi.fetchBook('123');

            expect(result).toEqual(book);
        });
    });

    describe('create book', () => {
        test('should post to /books', async () => {
            const createBook = generateCreateBook();
            const createdBook = generateBook();
            vi.spyOn(baseApi, 'post').mockResolvedValue({status: 201, data: createdBook});

            await bookApi.createBook(createBook);

            expect(baseApi.post).toHaveBeenCalledWith('/books', createBook);
        });

        test('returns the created book', async () => {
            const createBook = generateCreateBook();
            const createdBook = generateBook();
            vi.spyOn(baseApi, 'post').mockResolvedValue({status: 201, data: createdBook});

            const result = await bookApi.createBook(createBook);

            expect(result).toEqual(createdBook);
        });

        test('returns problem detail when backend returns validation error', async () => {
            const createBook = generateCreateBook({title: ''});
            const problemDetail = {
                title: 'Bad Request',
                status: 400,
                detail: 'Validation failed',
                errors: {title: 'must not be blank'},
            };
            const error = new AxiosError('Bad Request', 'ERR_BAD_REQUEST', undefined, undefined, {
                status: 400, data: problemDetail,
            } as never);
            vi.spyOn(baseApi, 'post').mockRejectedValue(error);

            await expect(bookApi.createBook(createBook)).rejects.toThrow(ProblemDetailError);
        });

        test('re-throws non-HTTP errors as-is', async () => {
            const createBook = generateCreateBook();
            const networkError = new Error('Network Error');
            vi.spyOn(baseApi, 'post').mockRejectedValue(networkError);

            await expect(bookApi.createBook(createBook)).rejects.toThrow(networkError);
        });
    });

    describe('update book', () => {
        test('should put to /books/{id}', async () => {
            const updateBook = generateUpdateBook();
            vi.spyOn(baseApi, 'put').mockResolvedValue({data: undefined});

            await bookApi.updateBook('456', updateBook);

            expect(baseApi.put).toHaveBeenCalledWith('/books/456', updateBook);
        });

        test('returns void', async () => {
            const updateBook = generateUpdateBook();
            vi.spyOn(baseApi, 'put').mockResolvedValue({data: undefined});

            const result = await bookApi.updateBook('456', updateBook);

            expect(result).toBeUndefined();
        });
    });

    describe('delete book', () => {
        test('should delete /books/{id}', async () => {
            vi.spyOn(baseApi, 'delete').mockResolvedValue({data: undefined});

            await bookApi.deleteBook('789');

            expect(baseApi.delete).toHaveBeenCalledWith('/books/789');
        });

        test('returns void', async () => {
            vi.spyOn(baseApi, 'delete').mockResolvedValue({data: undefined});

            const result = await bookApi.deleteBook('789');

            expect(result).toBeUndefined();
        });
    });
});
