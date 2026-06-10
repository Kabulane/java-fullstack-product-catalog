# Codex Log

## 2026-06-10 - Backend skeleton

Prompt summary: initialize the Maven-based Spring Boot backend with Java 25, PostgreSQL, Flyway, validation, OpenAPI, a health endpoint, and a context test.

Decisions:
- Spring Boot 4.0.6 because it officially supports Java 25.
- springdoc-openapi 3.0.3 because springdoc 3.x is the compatible line for Spring Boot 4.
- Spring Boot's Flyway starter is used because Boot 4 packages Flyway auto-configuration as a dedicated module.
- H2 is test-scoped so the context smoke test is repeatable without requiring the local PostgreSQL container.
- Database settings use local defaults matching Docker Compose and allow environment-variable overrides.
