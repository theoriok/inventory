import {render, screen, waitFor} from '@testing-library/react';
import {afterEach, beforeEach, describe, expect, it, vi} from 'vitest';

import {generateBook} from '../__test__/generators/book.generator.ts';
import {bookApi} from '../api/book.api.ts';
import {Book,} from '../api/book.api.types.ts';
import {App} from '../App.tsx';

afterEach(() => {
    window.location.hash = '/';
});

describe('list books', () => {
    describe('no books', () => {
        beforeEach(async () => given([]));

        it('has a title', async () => await waitFor(() => {
            const titleHeading = screen.getByRole('heading');
            expect(titleHeading).toBeInTheDocument()
            expect(titleHeading).toHaveTextContent("Books")
        }));

        it('shows the no books message', async () => await waitFor(() =>
            expect(screen.getByTestId('no-books-message')).toBeInTheDocument(),
        ));

        it('shows the add books button', async () => await waitFor(() =>
            expect(screen.getByTestId('add-books')).toBeInTheDocument(),
        ));
    });

    describe('one book', () => {
        const book = generateBook();
        beforeEach(async () => given([book]));

        it('shows the book', async () => await waitFor(() => {
            const booksTable = screen.getByTestId('books-table');
            expect(booksTable).toBeInTheDocument()
            expect(booksTable).toHaveTextContent(book.title)
            expect(booksTable).toHaveTextContent(book.author)
            expect(booksTable).toHaveTextContent(book.description)
        }));

        it('shows the add books button', async () => await waitFor(() =>
            expect(screen.getByTestId('add-books')).toBeInTheDocument(),
        ));
    });
});

function given(books: Book[]) {
    vi.spyOn(bookApi, 'fetchBooks').mockResolvedValue({
        items: books,
        total: books.length,
    });
    vi.spyOn(bookApi, 'fetchBook').mockImplementation((id) =>
        Promise.resolve(books.find((book) => book.business_id === id)!),
    );
    render(<App/>);
}
