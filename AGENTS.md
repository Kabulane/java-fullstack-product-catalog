# Project instructions for Codex

## Goal

Build a fullstack product catalog test project with:
- Java 25
- Spring Boot
- PostgreSQL
- Flyway
- Vue 3
- Docker Compose

The project must be simple, professional, readable, and easy to defend in a technical interview.

## AI usage

Codex is used as a modern development assistant, not as a blind code generator.

Every generated contribution must be:
- reviewed by the developer;
- understood before being kept;
- integrated progressively;
- tested;
- documented when it involves a technical decision.

Important prompts and decisions must be tracked in `docs/codex-log.md`.

## Architecture

Use a clean Spring classic architecture:
- controller
- service
- repository
- entity
- DTO

Do not implement a full hexagonal architecture unless it clearly adds value.

Use dedicated DTOs per endpoint/use case.

## Tests

Even if the test statement does not explicitly require tests, automated tests are mandatory.

Include:
- unit tests for dashboard/statistics services;
- integration test for JSON import;
- API test for main catalog endpoints;
- repository test if useful.

Tests must be simple, readable, and runnable with Maven.

## Documentation

Keep documentation concise:
- README with setup and run commands;
- architecture decisions in `docs/architecture-decisions.md`;
- Codex prompts in `docs/codex-log.md`.

## Constraints

- No over-engineering.
- No hidden manual setup.
- The project must run locally from a fresh clone.
- Prefer clarity over cleverness.