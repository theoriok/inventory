import {faker} from '@faker-js/faker';

import {Country} from '../../api/country.api.types.ts';

export function generateCountry(overrides: Partial<Country> = {}): Country {
    return {
        name: faker.location.country(),
        code: faker.location.countryCode(),
        ...overrides,
    };
}
