# Steering File

## General

- Don't make changes the user didn't ask for. If you spot something that needs fixing, mention it and wait for approval.
- When the user says "revert", revert exactly what was asked — nothing more, nothing less.
- Run tests after every code change unless the user explicitly says not to.
- When a build fails, report the error and suggest a fix, but wait for the user's input before changing anything.
- The repo might use Windows line endings (`\r\n`) for some files. Use `create` to rewrite files when `str_replace` fails due to line ending mismatches. Don't shell out to `sed`.
- Prefer the `code` tool over `fs_read` over `cat` for reading. Prefer the `code` tool over `fs_write` over `sed` for writing.

## Code Style

- Follow existing code conventions — look at how similar things are already done in the codebase before writing new code.
- Keep code minimal. Don't add boilerplate or defensive code that isn't needed.

## Planning

- When asked to make a plan, write it as a `.md` file in the project root.
- Be thorough in plans — include all affected files, code examples, and edge cases.
- Be critical when reviewing plans — call out inconsistencies and risks before implementing.

## Communication

- Don't second-guess the user's instructions. If they say to do something, do it.
- Don't explain why something is obvious. Just do it.
- If you're unsure about something, ask — don't guess and get it wrong.
- When the user points out an error, fix it directly. Don't over-explain or rationalize the mistake.
