import {faker} from '@faker-js/faker';

import {Book, CreateBook} from '../../api/book.api.types.ts';

export function generateBook(overrides: Partial<Book> = {}): Book {
    return {
        id: faker.string.uuid(),
        author: faker.book.author(),
        title: faker.book.title(),
        description: faker.lorem.sentences(5),
        ...overrides,
    };
}

export function generateCreateBook(overrides: Partial<CreateBook> = {}): CreateBook {
    return {
        author: faker.book.author(),
        title: faker.book.title(),
        description: faker.lorem.sentences(5),
        ...overrides,
    };
}

