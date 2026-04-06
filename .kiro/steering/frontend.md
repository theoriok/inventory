# Frontend Steering

## General

- Run `yarn test` from `fe/inventory-frontend` after every code change unless the user explicitly says not to.
- Run `yarn lint` after code changes as well.
- Capture the full test output. Don't pipe through `tail` or `head` — you'll miss important context and end up running them again.
- package.json contains which yarn commands you can run. Use these instead of doing weird npx stuff.

## File Naming

- `{domain}.{layer}.ts` for code, `{domain}.{layer}.types.ts` for types, `{domain}.{layer}.spec.ts(x)` for tests.
- Generators: `{domain}.generator.ts` in `src/__test__/generators/`.

## Linting & Type Safety

- No Prettier or formatter — style is enforced by convention, not tooling.
- `tsconfig.json` has `strict: true`, `noUnusedLocals`, `noUnusedParameters`, and `noFallthroughCasesInSwitch`. Don't leave unused variables or imports.
- ESLint uses `@eslint/js` recommended + `typescript-eslint` recommended. Key enforced rules:
  - No `any` (`no-explicit-any`). Use proper types.
  - No unused vars (`no-unused-vars`).
  - No floating promises (`no-floating-promises`). Always `await` or return promises.
  - No `require()` — ESM imports only (`no-require-imports`).
  - No `@ts-ignore` without description (`ban-ts-comment`).
  - No unsafe `any` operations (`no-unsafe-argument`, `no-unsafe-assignment`, `no-unsafe-call`, `no-unsafe-member-access`, `no-unsafe-return`).
  - `require-await` — don't mark functions `async` if they don't `await`.
  - `restrict-template-expressions` — only strings in template literals.
- `react-hooks/rules-of-hooks` and `react-hooks/exhaustive-deps` are enforced.
- `react-refresh/only-export-components` is a warning (with `allowConstantExport: true`).

## Code Style

- React with TypeScript, Vite, Ant Design.
- Use functional components and hooks.
- Use TanStack Query for data fetching.
- Use `axios` for HTTP calls.
- 4-space indentation in `.ts`/`.tsx` files (convention, not enforced by tooling).
- LF line endings, final newline (`.editorconfig`).
- Named exports everywhere, no default exports.
- Use `const` for variables, `function` for hooks and generators.
- API modules are typed `const` objects implementing an interface, not classes.
- Destructure axios responses with explicit type annotation: `const {data: book}: AxiosResponse<Book> = await baseApi.get(...)`.
- Use `FC` type annotation on components.
- Hooks are plain exported functions, not arrow functions assigned to consts.
- Query key enums live in the hook file, not in a separate constants file.
- Imports use `.ts`/`.tsx` extensions.
- Use `data-testid` for test selectors.
- Prefer semantic faker methods (e.g. `faker.book.author()` not `faker.person.fullName()`).

## Testing

- Use Vitest with React Testing Library.
- Use `@faker-js/faker` for test data generation.
- Test behavior, not implementation details.
- Use `describe`/`test` consistently, not `it`.
- Arrange/act/assert separated by blank lines.
- Generators accept `Partial<T>` overrides spread last.
- Integration tests use a `given()` helper with stateful spies on the API object — not HTTP-level mocking.
- Table column indices are named constants, not magic numbers.
