# Football-Field-Management-BE

Spring Boot backend for the Football Field Management & Booking System.

This repository contains the finalized **Phase 1** code-first persistence foundation and **Phase 2A** authentication/account flow. The 22-table persistence model remains unchanged.

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
| `JWT_SECRET` | none | Required HMAC secret for JWT signing; use at least 32 bytes |
| `ADMIN_EMAIL` | `admin@gmail.com` (local profile only) | Local development admin email |
| `ADMIN_PASSWORD` | `Admin@123` (local profile only) | Local development admin password |

Example for PowerShell (use your own PostgreSQL password):

```powershell
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "football_field_management"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "your-local-password"
$env:JWT_SECRET = "use-a-local-secret-with-at-least-32-bytes"
```

No passwords, API keys, payment credentials, or Supabase keys belong in source control.

### Local development admin account

When `SPRING_PROFILES_ACTIVE=local` is active, application startup creates this account only when its email does not already exist:

| Field | Value |
| --- | --- |
| Email | `admin@gmail.com` |
| Password | `Admin@123` |
| Role | `ADMIN` |
| Active | `true` |

**THIS IS A LOCAL DEVELOPMENT ACCOUNT ONLY.** Production or deployed environments must provide their own secure account provisioning and must not rely on these default credentials. The seeder never overwrites an existing admin password.

## Build and run

```bash
mvn clean compile
mvn test
mvn spring-boot:run
```

The application uses `spring.jpa.hibernate.ddl-auto=update` in `application-local.yml`. Hibernate Code First creates or updates the local PostgreSQL schema from the JPA entities. There are no Flyway migrations or manual `CREATE TABLE` scripts.

## Authentication API

All responses use this shape:

```json
{
  "success": true,
  "message": "...",
  "data": {}
}
```

| Method | Path | Access |
| --- | --- | --- |
| `POST` | `/api/auth/register` | Public; creates `CUSTOMER` accounts only |
| `POST` | `/api/auth/login` | Public; returns a 24-hour JWT access token |
| `GET` | `/api/users/me` | Bearer JWT required |
| `PUT` | `/api/users/me` | Bearer JWT required; updates only `fullName`, `phone`, and `avatarUrl` |

JWT access tokens use HS256 and include `sub` (user ID), `email`, and `role`. They expire after 86,400 seconds (24 hours). Passwords are stored only as BCrypt hashes and never appear in API responses.

There is no refresh-token or backend logout endpoint. Flutter logout must delete its locally stored access token and navigate to Login.

Swagger UI is public at `/swagger-ui.html`. Use its **Authorize** button with the `Bearer JWT` scheme after logging in, then call `/api/users/me`.

## Current boundaries

- Docker is not used yet.
- Redis is included as a dependency foundation and does not need to be running.
- Supabase Storage is not integrated; image fields store only URL/path strings.
- Spring AI Google GenAI is included as a dependency foundation, but AI functionality is not implemented and no API key is required for startup.
- The only business APIs implemented are the Phase 2A authentication/account endpoints above.
- Payment gateway, other business APIs, caches, and CRUD services are not implemented yet.

Swagger UI is available at `/swagger-ui.html`.
