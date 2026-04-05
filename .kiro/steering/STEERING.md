# Steering File

## General

- Don't make changes the user didn't ask for. If you spot something that needs fixing, mention it and wait for approval.
- When refactoring multiple files, do one file at a time and run tests between each (unless this makes it not compile, then you can do the necessary changes to make it compile as well). Don't batch all files into one change.
- Don't split up changes in the same file into multiple write commands if they actually belong together.
- When the user says "revert", revert exactly what was asked — nothing more, nothing less.
- Run tests after every code change unless the user explicitly says not to.
- When a build fails, report the error and suggest a fix, but wait for the user's input before changing anything.
- The repo might use Windows line endings (`\r\n`) for some files. Use `create` to rewrite files when `str_replace` fails due to line ending mismatches. Don't shell out to `sed`.
- Prefer the `code` tool over `fs_read` over `cat` for reading. Prefer the `code` tool over `fs_write` over `sed` for writing.
- Don't pipe command output through `head` or `tail` when debugging — you lose visibility into whether commands are hanging, and important context gets truncated, leading to re-runs.

## TDD

- Follow red → green → refactor strictly. Don't skip or rush the refactor step.
- Refactor applies to both production code and test code. Tests are the spec — they deserve the same attention for consistency, clarity, and duplication.
- After green, review both production and test code, share observations, and ask the user before moving on — even if you see nothing to change. We're mobbing, don't fly solo.
- Think about how the code will actually be used. Don't dismiss obviously needed behavior as "not needed yet".
- A proper red state means tests run and fail on assertions, not compilation or import errors. When testing a new module, create a minimal stub with existing but non-functional methods so the tests can actually execute.

## Code Style

- Follow existing code conventions — look at how similar things are already done in the codebase before writing new code.
- Keep code minimal. Don't add boilerplate or defensive code that isn't needed.
- Verify library API signatures before making claims about what's possible or not. Check the docs or source — don't assume based on older knowledge.

## Planning

- When asked to make a plan, write it as a `.md` file in the project root.
- Be thorough in plans — include all affected files, code examples, and edge cases.
- Be critical when reviewing plans — call out inconsistencies and risks before implementing.

## Communication

- Don't second-guess the user's instructions. If they say to do something, do it.
- Don't explain why something is obvious. Just do it.
- If you're unsure about something, ask — don't guess and get it wrong.
- When the user points out an error, fix it directly. Don't over-explain or rationalize the mistake.
- Don't run git commands to recall what was just done — use the conversation context.
- Keep commit messages functional and concise. Describe what the change does, not a file-by-file listing.
- When presenting a list of suggestions, ask how the user wants to review them (one by one, all at once, etc.) rather than assuming.
- Don't pad lists. If there's one option, present one option. If a summary references N items, include all N.
- When stuck on a library behavior issue, search online for known issues and common solutions before diving into source code. Look at multiple results from the same search before trying a different search.
- When trying to fix something, propose one approach and ask before trying it. Don't chain multiple speculative changes without checking in.
