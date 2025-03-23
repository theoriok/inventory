import {Book, BookApi, ListResponse} from "./book.api.types.ts";
import {baseApi} from './base.api.ts';
import {AxiosResponse} from "axios";

export const bookApi: BookApi = {
    async fetchBook(business_id: string): Promise<Book> {
        const {data: book}: AxiosResponse<Book> = await baseApi.get(`/books/${business_id}`);
        return book;
    },

    async fetchBooks(): Promise<ListResponse<Book>> {
        const {data: books}: AxiosResponse<Book[]> = await baseApi.get('/books');
        return {items: books, total: books.length};
    }
}
