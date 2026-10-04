# Football-Field-Management-BE

Spring Boot backend for the Football Field Management & Booking System.

This repository currently contains **Phase 1: backend bootstrap and code-first database foundation**. It defines the finalized JPA persistence model only; business APIs and CRUD flows are intentionally not implemented yet.

## Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL

## Create the local database

Create an empty PostgreSQL database before starting the application:

```sql
CREATE DATABASE football_field_management;
```

## Configure local environment variables

The local profile is selected by default. It reads the database connection from environment variables:

| Variable | Default | Description |
| --- | --- | --- |
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `football_field_management` | Database name |
| `DB_USERNAME` | `postgres` | Database user |
| `DB_PASSWORD` | none | Database password; must be supplied locally |

Example for PowerShell (use your own PostgreSQL password):

```powershell
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "football_field_management"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "your-local-password"
```

No passwords, API keys, payment credentials, or Supabase keys belong in source control.

## Build and run

```bash
mvn clean compile
mvn test
mvn spring-boot:run
```

The application uses `spring.jpa.hibernate.ddl-auto=update` in `application-local.yml`. Hibernate Code First creates or updates the local PostgreSQL schema from the JPA entities. Phase 1 deliberately has no Flyway migrations or manual `CREATE TABLE` scripts.

## Phase 1 boundaries

- Docker is not used yet.
- Redis is included as a dependency foundation and does not need to be running.
- Supabase Storage is not integrated; image fields store only URL/path strings.
- Spring AI Google GenAI is included as a dependency foundation, but AI functionality is not implemented and no API key is required for startup.
- Payment gateway, business APIs, authentication flow, caches, and CRUD services are not implemented yet.

Swagger UI is prepared at `/swagger-ui.html` for later API work.
