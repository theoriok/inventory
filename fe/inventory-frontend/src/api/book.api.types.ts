export interface BookApi {
    fetchBooks(): Promise<ListResponse<Book>>;

    fetchBook(id: string): Promise<Book>;

    createBook(book: CreateBook): Promise<Book>;
}

export interface CreateBook {
    title: string;
    author: string;
    description: string;
}

export interface Book {
    id: string;
    title: string;
    author: string;
    description: string;
}

export interface ListResponse<T> {
    total: number;
    items: T[];
}
