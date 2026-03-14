# Steering File

## General

- Don't make changes the user didn't ask for. If you spot something that needs fixing, mention it and wait for approval.
- When the user says "revert", revert exactly what was asked — nothing more, nothing less.
- Run `./gradlew clean check` after every code change unless the user explicitly says not to.
- When a build fails, report the error and suggest a fix, but wait for the user's input before changing anything.

## Code Style

- No Lombok. Use plain Java classes and records.
- Versions go in the dependency management block of the root `build.gradle`, not in individual module build files.
- Follow existing code conventions — look at how similar things are already done in the codebase before writing new code.
- When classes are in the same package, don't add unnecessary imports.
- Keep code minimal. Don't add boilerplate or defensive code that isn't needed.

## Architecture

- This project follows hexagonal/clean architecture with ports and adapters.
- Domain differences between entities are acceptable. Not everything needs to be identical — only align what is genuinely inconsistent.
- When aligning two similar domains, be critical about what is a real inconsistency vs. a legitimate domain difference.

## Testing

- Use the actual ID from saved entities, not static constants. Save the entity, capture the returned object, use its ID.
- Use random Vocabulary Ids for not-found test cases to guarantee the ID doesn't exist. Do not use UUID directly even if you know the underlying value is a UUID.
- Always verify full response bodies.
- When expected JSON needs a dynamic ID, use `%s` placeholders and `.formatted()` at the call site.
- Don't delete or modify tests if they fail unless explicitly asked. Usually the tests fail because the production code is wrong.

## Planning

- When asked to make a plan, write it as a `.md` file in the project root.
- Be thorough in plans — include all affected files, code examples, and edge cases.
- Be critical when reviewing plans — call out inconsistencies and risks before implementing.

## Communication

- Don't second-guess the user's instructions. If they say to do something, do it.
- Don't explain why something is obvious. Just do it.
- If you're unsure about something, ask — don't guess and get it wrong.
- When the user points out an error, fix it directly. Don't over-explain or rationalize the mistake.
