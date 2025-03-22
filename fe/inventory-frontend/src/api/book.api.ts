import {Book, BookApi, ListResponse} from "./book.api.types.ts";
import {baseApi} from './base.api.ts';

export const bookApi: BookApi = {
    async fetchBook(business_id: string): Promise<Book> {
        return baseApi.get(`books/${business_id}`)
    },

    async fetchBooks(): Promise<ListResponse<Book>> {
        const {data} = await baseApi.get('books');
        return {items: data, total: data.length};
    }
}
