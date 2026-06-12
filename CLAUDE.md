# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A university student cabinet pet project, backend-focused: a Spring Boot REST API (`student-api`), a small React + Vite demo client (`student-cabinet`), and a `mock-1c` Node service that stands in for the university's real 1C system. The API models a student's profile, grades, schedule, and academic debts, periodically syncing them from a 1C-shaped HTTP source into its own database.

## Commands

### Mock 1C (`mock-1c/`)

A dependency-free Node HTTP server that imitates the 1C HTTP API. Must be running before the backend starts in `demo` or `postgres` profile (both default to `app.integration.onec.mode: http` and sync on startup).

```bash
cd mock-1c
node server.js
```

Serves `GET /onec/students/{id}/{profile|grades|schedule|debts}` on `http://127.0.0.1:8091` for demo students `2021-301-047`, `2021-301-048`, `2022-402-015`, `2020-104-033`.

### Backend (`student-api/`)

H2 in-memory demo profile (data does not persist across restarts; resynced from mock-1C on every startup):

```bash
cd student-api
mvn -Dspring-boot.run.profiles=demo spring-boot:run
```

PostgreSQL profile (data persists; closer to the real integration):

```bash
docker compose up -d postgres   # from repo root
cd student-api
mvn -Dspring-boot.run.profiles=postgres spring-boot:run
```

Build / run tests:

```bash
mvn test
mvn package
```

Requires a JDK (not just a JRE) — `JAVA_HOME` must point at a JDK 8+ install for `mvn` to compile.

### Frontend (`student-cabinet/`)

```bash
cd student-cabinet
npm install
npm run dev          # vite, binds to localhost only (IPv6 [::1])
npm run dev -- --host   # or `npm run dev:host`, binds 0.0.0.0 — needed if localhost ERR_CONNECTION_REFUSED on IPv4
npm run lint
npm run build
npm run preview
```

Dev server runs on `http://localhost:5173`. It talks to the API at `VITE_API_BASE_URL` (default `http://localhost:8080`, see `.env.example`).

### Demo login

```text
login: 2021-301-047
password: password
```

Seeded via `V2__create_app_users.sql` (BCrypt hash).

## Architecture

### Data flow

```
mock-1c (Node, :8091)
  -> OneCStudentDataProvider (RestTemplate, mode: http)
    -> OneCSyncService (on ApplicationReadyEvent + @Scheduled)
      -> students / grades / schedule_days / lessons / debts tables (H2 or Postgres)
        -> StudentCabinetService (reads from repositories, scoped to the authenticated user)
          -> StudentController (/api/student/*)
```

`StudentCabinetService` no longer calls `StudentDataProvider` directly — it reads synced rows from `StudentRepository`/`GradeRepository`/`ScheduleDayRepository`/`DebtRepository`, resolved via the authenticated user's `app_users.student_onec_id` -> `students.onec_id`. If a user has no linked student or the student hasn't been synced yet, the service throws `IllegalStateException`.

`OneCSyncService` runs `syncDemoStudents()` (iterating `app.integration.onec.demo-student-ids`) once on startup and again on a `@Scheduled` fixed delay (`app.integration.onec.sync-fixed-delay-ms`, default 10 min). It's disabled via `app.integration.onec.sync-enabled: false`. Each sync upserts a `StudentEntity` (matched by `onec_id`) and replaces its grades/schedule/debts wholesale (delete-then-insert), using deterministic `UUID.nameUUIDFromBytes` ids so re-syncs are idempotent.

### Request flow (auth)

```
HTTP request
  -> Spring Security filter chain (stateless, CSRF disabled, CORS configured)
    -> JwtAuthenticationFilter (reads "Authorization: Bearer <token>", loads AppUserEntity, sets SecurityContext)
      -> AuthController (/api/auth/login) / StudentController (/api/student/*)
        -> AuthService / StudentCabinetService
          -> AppUserRepository / domain repositories
            -> PostgreSQL or H2 (demo)
```

- `JwtAuthenticationFilter` is the only auth mechanism; there's no `UsernamePasswordAuthenticationFilter` usage — it's a pure token-based stateless setup (`SecurityConfig`).
- `/api/auth/login`, `/actuator/health`, and `/h2-console/**` are the only endpoints permitted without a token; everything else under `/api/**` requires `Authorization: Bearer <token>`.
- Passwords are BCrypt-hashed in `app_users` (`AppUserEntity` / `AppUserRepository`); `AuthService.login()` verifies and issues a JWT via `JwtService`.

### 1C integration boundary

`StudentDataProvider` (`integration/StudentDataProvider.java`) is implemented by:

- `MockOneCStudentDataProvider` (`mode: mock`) — returns hardcoded demo data in-process; not used by `demo`/`postgres` profiles anymore but kept for `mode: mock`.
- `OneCStudentDataProvider` (`mode: http`, default for `demo`/`postgres`) — calls `mock-1c` (or a real 1C HTTP endpoint) via `RestTemplate` (`HttpClientConfig`), configured through `OneCProperties` (`app.integration.onec.base-url/username/password/sync-*/demo-student-ids`).

Only `OneCSyncService` consumes `StudentDataProvider` directly; the REST layer always reads from the local DB.

### Database schema (Flyway)

Migrations in `student-api/src/main/resources/db/migration`:
- `V1__init_student_cabinet.sql` — domain tables: `students`, `grades`, `schedule_days`, `lessons`, `debts` (each row tagged with `synced_at`, populated by `OneCSyncService`).
- `V2__create_app_users.sql` — `app_users` table + seeded demo user (`student_onec_id = '2021-301-047'`).

`ddl-auto: validate` — schema changes must go through new Flyway migrations, not Hibernate auto-DDL.

### Frontend structure

Single-file React app (`src/App.jsx`) with a tiny hand-rolled `api` client (fetch wrapper adding the bearer token). Session (JWT + login) is persisted to `localStorage` under `student-session`. Pages: Grades, Schedule, Debts, Profile, all driven by the four `/api/student/*` endpoints fetched once after login.

## Configuration notes

- `application.yml`: base config (Postgres datasource, JWT secret/TTL via `JWT_SECRET`/`JWT_TTL_MINUTES`, CORS allowed origins via `CORS_ALLOWED_ORIGINS`, 1C integration mode/credentials).
- `application-demo.yml` (`demo` profile): in-memory H2 (Postgres-compatible mode), H2 console enabled, `mode: http` against `http://127.0.0.1:8091` (mock-1C), sync enabled.
- `application-postgres.yml` (`postgres` profile): real Postgres datasource (`DB_URL`/`DB_USERNAME`/`DB_PASSWORD`, defaults match `docker-compose.yml`), `mode: http`, sync configurable via `ONEC_BASE_URL`/`ONEC_SYNC_ENABLED`/`ONEC_SYNC_FIXED_DELAY_MS`.
- `docker-compose.yml` (repo root): local Postgres 16 for the `postgres` profile.
- Real 1C endpoints/credentials, `.env` files, DB dumps, and logs must never be committed.
