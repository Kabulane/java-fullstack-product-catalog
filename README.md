# Product Catalog

Fullstack product catalog technical test with a Vue dashboard, catalog browsing, product details, reviews, and dashboard statistics. Product data is imported from `data/products.json` when the backend starts with an empty database.

## Stack

- Backend: Java 25, Spring Boot 4, Spring Data JPA, Flyway, PostgreSQL
- API documentation: Springdoc OpenAPI and Swagger UI
- Frontend: Vue 3, Vite, PrimeVue, Axios
- Infrastructure: Docker Compose

## Prerequisites

- Java 25
- Maven 3.9+
- Node.js and npm
- Docker with Docker Compose

## Run Locally

Start PostgreSQL from the project root:

```bash
docker compose up -d
```

Start the backend:

```bash
cd backend
mvn spring-boot:run
```

The API is available at `http://localhost:8080`.

In another terminal, install frontend dependencies and start Vite:

```bash
cd frontend
npm install
npm run dev
```

The frontend is available at `http://localhost:5173`.

## Tests and Build

Run backend tests:

```bash
cd backend
mvn test
```

Build the frontend:

```bash
cd frontend
npm install
npm run build
```

## API Documentation

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Main API Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/health` | Backend health check |
| `GET` | `/api/products` | Full product catalog |
| `GET` | `/api/products/{reference}` | Product details |
| `GET` | `/api/products/{reference}/reviews` | Product reviews |
| `GET` | `/api/dashboard/summary` | Product and review totals |
| `GET` | `/api/dashboard/most-appreciated-products` | Most appreciated products |
| `GET` | `/api/dashboard/lowest-rated-products` | Lowest-rated products |

## Technical Choices

- The backend uses a classic controller, service, repository, entity, and DTO structure for clarity.
- Flyway owns the PostgreSQL schema; Hibernate validates it instead of modifying it.
- API responses use dedicated DTOs rather than exposing JPA entities.
- The Vue frontend keeps API calls in small Axios modules and uses PrimeVue for consistent UI components.
- Automated tests cover service logic, API behavior, and JSON import.

## AI-Assisted Development

Codex was used as a development assistant. Generated contributions were reviewed, integrated progressively, tested, and documented in `docs/codex-log.md`.
