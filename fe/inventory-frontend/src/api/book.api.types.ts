export interface BookApi {
    fetchBooks(): Promise<ListResponse<Book>>;

    fetchBook(business_id: string): Promise<Book>;
}

export interface Book {
    business_id: string;
    title: string;
    author: string;
    description: string;
}

export interface ListResponse<T> {
    total: number;
    items: T[];
}
