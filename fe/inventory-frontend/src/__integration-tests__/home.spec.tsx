import {render, screen, waitFor, within} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {afterEach, beforeEach, describe, expect, it, vi} from 'vitest';

import {generateBook} from '../__test__/generators/book.generator.ts';
import {bookApi} from '../api/book.api.ts';
import {Book,} from '../api/book.api.types.ts';
import {App} from '../App.tsx';
import {BOOK_TABLE} from './constants.ts';
import {faker} from "@faker-js/faker";

afterEach(() => {
    window.location.hash = '/';
});
describe('home', () => {
    describe('list books', () => {
        describe('no books', () => {
            beforeEach(async () => given([]));

            it('has a title', async () => await waitFor(() => {
                const titleHeading = screen.getByRole('heading');
                expect(titleHeading).toBeInTheDocument()
                expect(titleHeading).toHaveTextContent("Books")
            }));

            it('shows the table headers', async () => await waitFor(() => {
                const booksTable = screen.getByTestId('books-table');
                
                const headers = within(booksTable).getAllByRole('columnheader');
                expect(headers[BOOK_TABLE.TITLE_COLUMN]).toHaveTextContent('Title');
                expect(headers[BOOK_TABLE.AUTHOR_COLUMN]).toHaveTextContent('Author');
                expect(headers[BOOK_TABLE.DESCRIPTION_COLUMN]).toHaveTextContent('Description');
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

            it('shows the table headers', async () => await waitFor(() => {
                const booksTable = screen.getByTestId('books-table');
                
                const headers = within(booksTable).getAllByRole('columnheader');
                expect(headers[BOOK_TABLE.TITLE_COLUMN]).toHaveTextContent('Title');
                expect(headers[BOOK_TABLE.AUTHOR_COLUMN]).toHaveTextContent('Author');
                expect(headers[BOOK_TABLE.DESCRIPTION_COLUMN]).toHaveTextContent('Description');
            }));

            it('shows the book data', async () => await waitFor(() => {
                const booksTable = screen.getByTestId('books-table');
                
                const rows = within(booksTable).getAllByRole('row');
                const dataRows = rows.slice(1);
                expect(dataRows).toHaveLength(1);
                
                const cells = within(dataRows[0]).getAllByRole('cell');
                expect(cells[BOOK_TABLE.TITLE_COLUMN]).toHaveTextContent(book.title);
                expect(cells[BOOK_TABLE.AUTHOR_COLUMN]).toHaveTextContent(book.author);
                expect(cells[BOOK_TABLE.DESCRIPTION_COLUMN]).toHaveTextContent(book.description);
            }));

            it('does not show the no books message', async () => await waitFor(() =>
                expect(screen.queryByTestId('no-books-message')).not.toBeInTheDocument(),
            ));

            it('shows the add books button', async () => await waitFor(() =>
                expect(screen.getByTestId('add-books')).toBeInTheDocument(),
            ));
        });
    });

    describe('add book', () => {
        beforeEach(async () => given([]));

        it('opens modal when add book button is clicked', async () => {
            const user = userEvent.setup();
            await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());

            await user.click(screen.getByTestId('add-books'));

            await waitFor(() => {
                expect(screen.getByText('Add new Book')).toBeInTheDocument();
                expect(screen.getByLabelText('title')).toBeInTheDocument();
                expect(screen.getByLabelText('author')).toBeInTheDocument();
                expect(screen.getByLabelText('description')).toBeInTheDocument();
            });
        });

        it('closes modal when close is clicked', async () => {
            const user = userEvent.setup();
            await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());

            await user.click(screen.getByTestId('add-books'));
            await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

            await user.click(screen.getByRole('button', {name: 'Close'}));

            await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());
        });

        it('closes modal when cancel is clicked', async () => {
            const user = userEvent.setup();
            await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());

            await user.click(screen.getByTestId('add-books'));
            await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

            await user.click(screen.getByRole('button', {name: 'Cancel'}));

            await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());
        });

        it('closes modal when escape key is pressed', async () => {
            const user = userEvent.setup();
            await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());

            await user.click(screen.getByTestId('add-books'));
            await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

            await user.keyboard('{Escape}');

            await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());
        });

        it('adds book to table when form is filled and saved', async () => {
            const user = userEvent.setup();
            const newBook = generateBook({id: undefined});

            await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());
            await user.click(screen.getByTestId('add-books'));
            await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

            await user.type(screen.getByLabelText('title'), newBook.title);
            await user.type(screen.getByLabelText('author'), newBook.author);
            await user.type(screen.getByLabelText('description'), newBook.description);

            await user.click(screen.getByRole('button', {name: 'Add'}));

            await waitFor(() => {
                const booksTable = screen.getByTestId('books-table');
                const rows = within(booksTable).getAllByRole('row');
                const dataRows = rows.slice(1);
                expect(dataRows).toHaveLength(1);
                
                const cells = within(dataRows[0]).getAllByRole('cell');
                expect(cells[BOOK_TABLE.TITLE_COLUMN]).toHaveTextContent(newBook.title);
                expect(cells[BOOK_TABLE.AUTHOR_COLUMN]).toHaveTextContent(newBook.author);
                expect(cells[BOOK_TABLE.DESCRIPTION_COLUMN]).toHaveTextContent(newBook.description);
            });

            await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());
        });
    });
});



let savedBooks: Book[] = []

function given(books: Book[]) {
    savedBooks = books;
    vi.spyOn(bookApi, 'fetchBooks').mockImplementation(() =>
        Promise.resolve({
            items: savedBooks,
            total: savedBooks.length,
        }),
    );
    vi.spyOn(bookApi, 'fetchBook').mockImplementation((id) =>
        Promise.resolve(savedBooks.find((book) => book.id === id)!),
    );
    vi.spyOn(bookApi, 'createBook').mockImplementation(async (book) => {
        const newBook = {
            id: faker.string.uuid(),
            ...book,
        };
        savedBooks.push(newBook);
        return newBook;
    });
    render(
        <App/>
    );
}
