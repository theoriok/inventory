export interface BookApi {
    fetchBooks(): Promise<ListResponse<Book>>;

    fetchBook(id: string): Promise<Book>;

    createBook(book: CreateBook): Promise<Book>;

    updateBook(id: string, book: UpdateBook): Promise<void>;

    deleteBook(id: string): Promise<void>;
}

export interface UpdateBook {
    title: string;
    author: string;
    description: string;
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
