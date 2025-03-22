import {faker} from '@faker-js/faker';

import {Book} from '../../api/book.api.types.ts';

export function generateBook(overrides: Partial<Book> = {}): Book {
    return {
        business_id: faker.string.uuid(),
        author: faker.book.author(),
        title: faker.book.title(),
        description: faker.lorem.sentences(5),
        ...overrides,
    };
}

