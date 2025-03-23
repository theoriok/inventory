import {describe, expect, test, vi} from 'vitest';

import {baseApi} from './base.api.ts';
import {bookApi} from './book.api.ts';
import {generateBook} from '../__test__/generators/book.generator.ts';

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
});
