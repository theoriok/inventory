# Backend Steering

## General

- Run `./gradlew clean check` from the `be/` directory after every code change unless the user explicitly says not to.
- Dependencies are upgraded continuously — expect bleeding-edge library versions. Don't assume an API doesn't exist based on older knowledge.

## Environment

- Integration tests use Testcontainers and require Docker. If Docker is unavailable, they fail with Ryuk/Testcontainers errors — this is not a code issue.

## Code Style

- No Lombok. Use plain Java classes and records.
- Versions go in the dependency management block of the root `build.gradle`, not in individual module build files.
- When classes are in the same package, don't add unnecessary imports.
- When rewriting a file with `create`, preserve all existing imports. Don't drop imports that were already there.

## Architecture

- This project follows hexagonal/clean architecture with ports and adapters.
- Domain differences between entities are acceptable. Not everything needs to be identical — only align what is genuinely inconsistent.
- When aligning two similar domains, be critical about what is a real inconsistency vs. a legitimate domain difference.

## Testing

- Use `jdbcAggregateTemplate.insert()` for test data setup, not `repository.save()`.
- Use the actual ID from saved entities, not static constants. Save the entity, capture the returned object, use its ID.
- Use random Vocabulary Ids for not-found test cases to guarantee the ID doesn't exist. Do not use UUID directly even if you know the underlying value is a UUID.
- Always verify full response bodies.
- When expected JSON needs a dynamic ID, use `%s` placeholders and `.formatted()` at the call site.
- Don't delete or modify tests if they fail unless explicitly asked. Usually the tests fail because the production code is wrong.
- When a production code change affects API responses (e.g. adding a response body), update the corresponding tests in the same change.
- Prefer outside-in integration tests or scenario tests with in-memory repository implementations over mock-based unit tests. Mocks couple tests to implementation details. If use case logic grows complex enough to warrant isolated tests, use in-memory implementations with contract tests to ensure they behave like the real adapters.
