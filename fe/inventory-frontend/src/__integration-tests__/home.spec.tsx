import {render, screen, waitFor, within} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {afterEach, beforeEach, describe, expect, test, vi} from 'vitest';

import {generateBook} from '../__test__/generators/book.generator.ts';
import {bookApi} from '../api/book.api.ts';
import {Book,} from '../api/book.api.types.ts';
import {InventoryApp} from '../App.tsx';
import {BOOK_TABLE} from './constants.ts';
import {faker} from "@faker-js/faker";
import {ProblemDetailError} from "../api/api.types.ts";

afterEach(() => {
    vi.restoreAllMocks();
    window.location.hash = '/';
});
describe('home', () => {
    describe('list books', () => {
        describe('no books', () => {
            beforeEach(async () => given([]));

            test('has a title', async () => await waitFor(() => {
                const titleHeading = screen.getByRole('heading');
                expect(titleHeading).toBeInTheDocument()
                expect(titleHeading).toHaveTextContent("Books")
            }));

            test('shows the table headers', async () => await waitFor(() => {
                const booksTable = screen.getByTestId('books-table');
                
                const headers = within(booksTable).getAllByRole('columnheader');
                expect(headers[BOOK_TABLE.TITLE_COLUMN]).toHaveTextContent('Title');
                expect(headers[BOOK_TABLE.AUTHOR_COLUMN]).toHaveTextContent('Author');
                expect(headers[BOOK_TABLE.DESCRIPTION_COLUMN]).toHaveTextContent('Description');
            }));

            test('shows the no books message', async () => await waitFor(() =>
                expect(screen.getByTestId('no-books-message')).toBeInTheDocument(),
            ));

            test('shows the add books button', async () => await waitFor(() =>
                expect(screen.getByTestId('add-books')).toBeInTheDocument(),
            ));
        });

        describe('one book', () => {
            const book = generateBook();
            
            beforeEach(async () => given([book]));

            test('shows the table headers', async () => await waitFor(() => {
                const booksTable = screen.getByTestId('books-table');
                
                const headers = within(booksTable).getAllByRole('columnheader');
                expect(headers[BOOK_TABLE.TITLE_COLUMN]).toHaveTextContent('Title');
                expect(headers[BOOK_TABLE.AUTHOR_COLUMN]).toHaveTextContent('Author');
                expect(headers[BOOK_TABLE.DESCRIPTION_COLUMN]).toHaveTextContent('Description');
            }));

            test('shows the book data', async () => await waitFor(() => {
                const booksTable = screen.getByTestId('books-table');
                
                const rows = within(booksTable).getAllByRole('row');
                const dataRows = rows.slice(1);
                expect(dataRows).toHaveLength(1);
                
                const cells = within(dataRows[0]).getAllByRole('cell');
                expect(cells[BOOK_TABLE.TITLE_COLUMN]).toHaveTextContent(book.title);
                expect(cells[BOOK_TABLE.AUTHOR_COLUMN]).toHaveTextContent(book.author);
                expect(cells[BOOK_TABLE.DESCRIPTION_COLUMN]).toHaveTextContent(book.description);
            }));

            test('does not show the no books message', async () => await waitFor(() =>
                expect(screen.queryByTestId('no-books-message')).not.toBeInTheDocument(),
            ));

            test('shows the add books button', async () => await waitFor(() =>
                expect(screen.getByTestId('add-books')).toBeInTheDocument(),
            ));
        });
    });

    describe('add book', () => {
        describe('happy path', () => {
            beforeEach(async () => given([]));

            test('opens modal when add book button is clicked', async () => {
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

            test('closes modal when close is clicked', async () => {
                const user = userEvent.setup();
                await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());

                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                await user.click(screen.getByRole('button', {name: 'Close'}));

                await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());
            });

            test('closes modal when cancel is clicked', async () => {
                const user = userEvent.setup();
                await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());

                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                await user.click(screen.getByRole('button', {name: 'Cancel'}));

                await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());
            });

            test('closes modal when escape key is pressed', async () => {
                const user = userEvent.setup();
                await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());

                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                await user.keyboard('{Escape}');

                await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());
            });

            test('adds book to table when form is filled and saved', async () => {
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

            test('resets form when modal is cancelled and reopened', async () => {
                const user = userEvent.setup();
                await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());

                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());
                await user.type(screen.getByLabelText('title'), 'some title');
                await user.click(screen.getByRole('button', {name: 'Cancel'}));
                await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());

                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                expect(screen.getByLabelText('title')).toHaveValue('');
                expect(screen.getByLabelText('author')).toHaveValue('');
                expect(screen.getByLabelText('description')).toHaveValue('');
            });

            test('resets form after successful submission and reopened', async () => {
                const user = userEvent.setup();
                const newBook = generateBook({id: undefined});
                await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());

                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());
                await user.type(screen.getByLabelText('title'), newBook.title);
                await user.type(screen.getByLabelText('author'), newBook.author);
                await user.type(screen.getByLabelText('description'), newBook.description);
                await user.click(screen.getByRole('button', {name: 'Add'}));
                await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());

                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                expect(screen.getByLabelText('title')).toHaveValue('');
                expect(screen.getByLabelText('author')).toHaveValue('');
                expect(screen.getByLabelText('description')).toHaveValue('');
            });
        });

        describe('validation', () => {
            beforeEach(async () => given([]));

            test('shows validation errors when submitting empty form', async () => {
                const user = userEvent.setup();
                await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());
                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                await user.click(screen.getByRole('button', {name: 'Add'}));

                await waitFor(() => {
                    expect(screen.getByText('Title is required')).toBeInTheDocument();
                    expect(screen.getByText('Author is required')).toBeInTheDocument();
                    expect(screen.getByText('Description is required')).toBeInTheDocument();
                });
                expect(screen.getByRole('dialog')).toBeInTheDocument();
                expect(bookApi.createBook).not.toHaveBeenCalled();
            });

            test('shows validation errors when submitting whitespace-only values', async () => {
                const user = userEvent.setup();
                await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());
                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                await user.type(screen.getByLabelText('title'), '   ');
                await user.type(screen.getByLabelText('author'), '   ');
                await user.type(screen.getByLabelText('description'), '   ');
                await user.click(screen.getByRole('button', {name: 'Add'}));

                await waitFor(() => {
                    expect(screen.getByText('Title is required')).toBeInTheDocument();
                    expect(screen.getByText('Author is required')).toBeInTheDocument();
                    expect(screen.getByText('Description is required')).toBeInTheDocument();
                });
                expect(screen.getByRole('dialog')).toBeInTheDocument();
                expect(bookApi.createBook).not.toHaveBeenCalled();
            });

            test('shows validation errors when fields exceed max length', async () => {
                const user = userEvent.setup();
                await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());
                await user.click(screen.getByTestId('add-books'));
                await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                await user.click(screen.getByLabelText('title'));
                await user.paste('a'.repeat(256));
                await user.click(screen.getByLabelText('author'));
                await user.paste('a'.repeat(256));
                await user.click(screen.getByLabelText('description'));
                await user.paste('a'.repeat(5001));
                await user.click(screen.getByRole('button', {name: 'Add'}));

                await waitFor(() => {
                    expect(screen.getByText('Title must be at most 255 characters')).toBeInTheDocument();
                    expect(screen.getByText('Author must be at most 255 characters')).toBeInTheDocument();
                    expect(screen.getByText('Description must be at most 5000 characters')).toBeInTheDocument();
                });
                expect(screen.getByRole('dialog')).toBeInTheDocument();
                expect(bookApi.createBook).not.toHaveBeenCalled();
            });
        });

        describe('backend errors', () => {
            describe('validation errors', () => {
                beforeEach(async () => given([], 'validation'));

                test('shows backend validation errors inline on form fields', async () => {
                    const user = userEvent.setup();
                    await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());
                    await user.click(screen.getByTestId('add-books'));
                    await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                    await user.type(screen.getByLabelText('title'), 'x');
                    await user.type(screen.getByLabelText('author'), 'x');
                    await user.type(screen.getByLabelText('description'), 'x');
                    await user.click(screen.getByRole('button', {name: 'Add'}));

                    await waitFor(() => {
                        expect(screen.getAllByText('must not be blank')).toHaveLength(2);
                    });
                    expect(screen.getByRole('dialog')).toBeInTheDocument();
                });
            });

            describe('unexpected errors', () => {
                beforeEach(async () => given([], 'server'));

                test('shows error notification for unexpected backend errors', async () => {
                    const user = userEvent.setup();
                    await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());
                    await user.click(screen.getByTestId('add-books'));
                    await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                    await user.type(screen.getByLabelText('title'), 'x');
                    await user.type(screen.getByLabelText('author'), 'x');
                    await user.type(screen.getByLabelText('description'), 'x');
                    await user.click(screen.getByRole('button', {name: 'Add'}));

                    await waitFor(() => {
                        expect(screen.getByText('Something went wrong')).toBeInTheDocument();
                    });
                    expect(screen.getByRole('dialog')).toBeInTheDocument();
                });
            });

            describe('unknown errors', () => {
                beforeEach(async () => given([], 'network'));

                test('shows generic error notification for non-backend errors', async () => {
                    const user = userEvent.setup();
                    await waitFor(() => expect(screen.getByTestId('add-books')).toBeInTheDocument());
                    await user.click(screen.getByTestId('add-books'));
                    await waitFor(() => expect(screen.getByRole('dialog')).toBeInTheDocument());

                    await user.type(screen.getByLabelText('title'), 'x');
                    await user.type(screen.getByLabelText('author'), 'x');
                    await user.type(screen.getByLabelText('description'), 'x');
                    await user.click(screen.getByRole('button', {name: 'Add'}));

                    await waitFor(() => {
                        expect(screen.getByText('Something went wrong. Please try again.')).toBeInTheDocument();
                    });
                    expect(screen.getByRole('dialog')).toBeInTheDocument();
                });
            });
        });
    });
});

let savedBooks: Book[] = []

function given(books: Book[], createError?: 'validation' | 'server' | 'network') {
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
        if (createError === 'validation') {
            throw new ProblemDetailError({
                title: 'Bad Request',
                status: 400,
                detail: 'Validation failed',
                errors: {title: 'must not be blank', author: 'must not be blank'},
            });
        }
        if (createError === 'server') {
            throw new ProblemDetailError({
                title: 'Internal Server Error',
                status: 500,
                detail: 'Something went wrong',
            });
        }
        if (createError === 'network') {
            throw new Error('Network failure');
        }
        const newBook = {
            id: faker.string.uuid(),
            ...book,
        };
        savedBooks.push(newBook);
        return newBook;
    });
    render(
        <InventoryApp/>
    );
}
