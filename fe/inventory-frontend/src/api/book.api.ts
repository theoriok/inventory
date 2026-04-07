import {Book, BookApi, CreateBook, UpdateBook} from "./book.api.types.ts";
import {ListResponse, ProblemDetailError} from "./api.types.ts";
import {baseApi} from './base.api.ts';
import {AxiosResponse, isAxiosError} from "axios";

export const bookApi: BookApi = {
    async fetchBook(id: string): Promise<Book> {
        const {data: book}: AxiosResponse<Book> = await baseApi.get(`/books/${id}`);
        return book;
    },

    async fetchBooks(): Promise<ListResponse<Book>> {
        const {data: books}: AxiosResponse<Book[]> = await baseApi.get('/books');
        return {items: books, total: books.length};
    },

    async createBook(book: CreateBook): Promise<Book> {
        const response = await baseApi.post('/books', book).catch((e: unknown) => {
            if (isAxiosError(e) && e.response) return e.response;
            throw e;
        });
        if (response.status === 201) {
            return response.data as Book;
        }
        throw new ProblemDetailError(response.data);
    },

    async updateBook(id: string, book: UpdateBook): Promise<void> {
        await baseApi.put(`/books/${id}`, book);
    },

    async deleteBook(id: string): Promise<void> {
        const response = await baseApi.delete(`/books/${id}`).catch((e: unknown) => {
            if (isAxiosError(e) && e.response) return e.response;
            throw e;
        });
        if (response.status !== 200) {
            throw new ProblemDetailError(response.data);
        }
    }
}
