# Codex Log

## 2026-06-10 - Backend skeleton

Prompt summary: initialize the Maven-based Spring Boot backend with Java 25, PostgreSQL, Flyway, validation, OpenAPI, a health endpoint, and a context test.

Decisions:
- Spring Boot 4.0.6 because it officially supports Java 25.
- springdoc-openapi 3.0.3 because springdoc 3.x is the compatible line for Spring Boot 4.
- Spring Boot's Flyway starter is used because Boot 4 packages Flyway auto-configuration as a dedicated module.
- H2 is test-scoped so the context smoke test is repeatable without requiring the local PostgreSQL container.
- Database settings use local defaults matching Docker Compose and allow environment-variable overrides.

## 2026-06-10 - Product catalog relational model

Prompt summary: implement the validated `products`, `authors`, and `reviews` model with Flyway and JPA entities.

Decisions:
- Product references are unique business identifiers while database identity columns remain the primary keys.
- Reviews belong to products with cascade deletion; author deletion remains restrictive.
- Author types use string enum persistence and database validation.
- Hibernate validates the Flyway-managed schema instead of creating or updating it.

## 2026-06-10 - JSON catalog import

Prompt summary: add Spring Data repositories and import `data/products.json` at application startup.

Decisions:
- Import runs only when the products table is empty, making repeated application starts idempotent.
- Each imported review creates its own author because the source data provides no stable author identifier.
- Authors are persisted before products; product cascading then persists attached reviews in one transaction.
- The importer uses Spring Boot 4's managed Jackson 3 `ObjectMapper`.
- The import path defaults to `../data/products.json` when the backend is the working directory.
