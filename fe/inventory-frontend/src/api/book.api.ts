import {Book, BookApi, ListResponse} from "./book.api.types.ts";
import {baseApi} from './base.api.ts';
import {AxiosResponse} from "axios";

export const bookApi: BookApi = {
    async fetchBook(business_id: string): Promise<Book> {
        return baseApi.get(`/books/${business_id}`)
    },

    async fetchBooks(): Promise<ListResponse<Book>> {
        const {data}: AxiosResponse<Book[]> = await baseApi.get('/books');
        return {items: data, total: data.length};
    }
}
