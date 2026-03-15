import {Book, BookApi, CreateBook, ListResponse} from "./book.api.types.ts";
import {baseApi} from './base.api.ts';
import {AxiosResponse} from "axios";

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
        const {data: createdBook}: AxiosResponse<Book> = await baseApi.post('/books', book);
        return createdBook;
    }
}
