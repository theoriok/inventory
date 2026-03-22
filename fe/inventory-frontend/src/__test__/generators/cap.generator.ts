import {faker} from '@faker-js/faker';

import {Cap, CreateCap, UpdateCap} from '../../api/cap.api.types.ts';

export function generateCap(overrides: Partial<Cap> = {}): Cap {
    return {
        id: faker.string.uuid(),
        name: faker.commerce.productName(),
        description: faker.lorem.sentences(5),
        amount: faker.number.int({min: 1, max: 100}),
        country: {
            name: faker.location.country(),
            code: faker.location.countryCode(),
        },
        ...overrides,
    };
}

export function generateCreateCap(overrides: Partial<CreateCap> = {}): CreateCap {
    return {
        name: faker.commerce.productName(),
        description: faker.lorem.sentences(5),
        amount: faker.number.int({min: 1, max: 100}),
        country: faker.location.countryCode(),
        ...overrides,
    };
}

export function generateUpdateCap(overrides: Partial<UpdateCap> = {}): UpdateCap {
    return {
        name: faker.commerce.productName(),
        description: faker.lorem.sentences(5),
        amount: faker.number.int({min: 1, max: 100}),
        country: faker.location.countryCode(),
        ...overrides,
    };
}
