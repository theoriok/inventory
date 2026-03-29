# Frontend Steering

## General

- Run `yarn test` from `fe/inventory-frontend` after every code change unless the user explicitly says not to.
- Run `yarn lint` after code changes as well.
- Capture the full test output. Don't pipe through `tail` or `head` — you'll miss important context and end up running them again.
- package.json contains which yarn commands you can run. Use these instead of doing weird npx stuff.

## Code Style

- React with TypeScript, Vite, Ant Design.
- Use functional components and hooks.
- Use TanStack Query for data fetching.
- Use `axios` for HTTP calls.

## Testing

- Use Vitest with React Testing Library.
- Use `@faker-js/faker` for test data generation.
- Test behavior, not implementation details.
