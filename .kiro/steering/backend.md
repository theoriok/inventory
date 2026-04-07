# Backend Steering

## General

- Run `./gradlew clean check` from the `be/` directory after every code change unless the user explicitly says not to.
- Dependencies are upgraded continuously — expect bleeding-edge library versions. Don't assume an API doesn't exist based on older knowledge.

## Environment

- Integration tests use Testcontainers and require Docker. If Docker is unavailable, they fail with Ryuk/Testcontainers errors — this is not a code issue.

## Code Style

- No Lombok. Use plain Java classes and records. Use records for all data classes
- Vocabulary types (e.g. BookId, CapId) are records with validation in the compact constructor. They wrap a String value and provide from(UUID), toUuid(), and random*Id() factory methods.
- Constructor injection, no @Autowired. One constructor per class.
- 4-space indentation. Opening braces on same line. Google-style checkstyle enforced with zero tolerance (maxErrors = 0, maxWarnings = 0).
- No star imports. No unused imports. No tabs.
- Static imports for constants and static methods (e.g. assertThat, status(), content()), grouped at the top of the import block.
- Use @Language("JSON") annotation on methods returning JSON strings for IDE support.
- Use var for local variables when the type is obvious from the right-hand side.
- Versions go in the dependency management block of the root `build.gradle`, not in individual module build files.
- When classes are in the same package, don't add unnecessary imports.

## Architecture

- This project follows DDD with hexagonal/clean architecture. Domain is the core — it defines ports (interfaces) and knows nothing about infrastructure, frameworks, or delivery mechanisms. Adapters implement ports and live in infrastructure.
- Dependencies point inward: infrastructure → application → domain → vocabulary. Never the other way.
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
- Tests are organized with @Nested classes per HTTP verb/operation (e.g. Find, Create, Update, Delete).
- Use @Language("JSON") on helper methods that return expected JSON strings.
- JSON assertions use json-unit-assertj (assertThatJson) for dynamic fields (e.g. ${json-unit.any-string} for generated IDs).
- Entity assertions use AssertJ's returns(..., from(...)) pattern.
- Validation tests use @ParameterizedTest with @MethodSource providing Arguments of (invalid input, expected error JSON).
- Expected JSON helpers use %s placeholders with .formatted() for dynamic IDs.

### Application Layer

- Commands are annotated with @Command (custom meta-annotation: @Component + @Transactional). Queries with @Query (@Component + @Transactional(readOnly = true)).
- Command/query interfaces define nested Request records and Result enums where needed.

### REST Layer

- Mapping is done with private methods in the controller (toBookDto, toCreateRequest, etc.).
- DTOs are records with @JsonProperty on each field. Input DTOs use @Validated + Jakarta validation annotations.
- Error responses use ProblemDetail (RFC 7807). Not-found returns ResponseEntity.of(ProblemDetail.forStatus(NOT_FOUND)).build().
- Delete/update results use switch expressions over the result enum.

### Persistence Layer

- Entities are records annotated with @Table and @Id. Entity IDs are UUID, not vocabulary types.
- Adapters use JdbcAggregateOperations (not repositories) for findAll, findOne, insert, update. Repositories are only used for custom queries (e.g. deleteByIdReturningCount).
- Mapping between entities and domain objects happens in the adapter with private methods.
