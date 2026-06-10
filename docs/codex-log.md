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

## 2026-06-10 - Catalog REST API

Prompt summary: expose product summaries, product details, and product reviews through DTO-based REST endpoints.

Decisions:
- Dedicated DTO records prevent JPA entities from being exposed by controllers.
- Product review counts and average ratings are calculated by aggregate repository projections.
- Review authors are loaded with a query-level fetch join while entity associations remain lazy.
- Average ratings are rounded to two decimal places; products without reviews return `0.00`.
- Unknown product references return HTTP 404 through a dedicated exception.
- Standalone MockMvc tests cover endpoint JSON and error behavior, while unit tests cover all `CatalogService` mapping and not-found branches.

## 2026-06-10 - Dashboard REST API

Prompt summary: add dedicated dashboard endpoints for KPI totals and ranked product aggregates.

Decisions:
- Dashboard aggregates use dedicated DTOs and repository projections instead of expanding catalog responses.
- Positive reviews are ratings strictly greater than 3.
- Ranked queries aggregate in the database and are limited to five results with deterministic tie-breakers.
- Lowest-rated products must have reviews and an average rating strictly below 3.
- Positive review counts are returned only for the most-appreciated response; the field is `null` for lowest-rated products.

## 2026-06-10 - Vue frontend skeleton

Prompt summary: initialize the Vue 3 bonus dashboard frontend with Vite, routing, PrimeVue, and Axios.

Decisions:
- PrimeVue 4 with the Aura preset provides the initial component styling.
- `/` redirects to `/dashboard`; catalog and product-detail views have dedicated routes.
- A shared Axios client targets `http://localhost:8080/api`.
- Views remain placeholders until frontend API integration is implemented.

## 2026-06-10 - Vue dashboard

Prompt summary: connect the Vue dashboard view to the backend dashboard endpoints.

Decisions:
- Dashboard API calls are isolated in `dashboardApi.js`.
- Summary and ranking requests load concurrently on component mount.
- The view keeps explicit loading, error, and data state.
- PrimeVue cards, tables, tags, messages, and spinner provide a responsive dashboard without custom visual complexity.

## 2026-06-10 - Local frontend CORS

Prompt summary: allow the Vite development server to call Spring Boot API endpoints.

Decisions:
- Spring MVC allows only `GET` requests from `http://localhost:5173` to `/api/**`.
- The configuration is intentionally limited to local frontend development and does not add security infrastructure.

## 2026-06-10 - Vue catalog

Prompt summary: connect the Vue catalogue and product-detail views to the catalog API.

Decisions:
- Catalog API calls are isolated in `catalogApi.js`, with route references URL-encoded.
- The full catalog loads once and uses client-side PrimeVue table pagination.
- Product detail and reviews load concurrently for the selected route reference.
- Named Vue Router navigation links connect catalog rows, product details, and the catalog return action.
- Both views maintain explicit loading and error states.
