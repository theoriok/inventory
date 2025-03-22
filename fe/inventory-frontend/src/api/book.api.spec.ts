import {describe, expect, it, vi} from 'vitest';

import {baseApi} from './base.api.ts';
import {bookApi} from './book.api.ts';
import {generateBook} from '../__test__/generators/book.generator.ts';

describe('BookApi', () => {
    it('returns the books', async () => {
        const books = [generateBook()];
        vi.spyOn(baseApi, 'get').mockResolvedValue(books);

        const result = await bookApi.fetchBooks();

        expect(result).toEqual({items: books, total: 1});
    });

    it('returns a single book', async () => {
        const book = generateBook();
        vi.spyOn(baseApi, 'get').mockResolvedValue(book);

        const result = await bookApi.fetchBook('123');

        expect(result).toEqual(book);
    });

});
