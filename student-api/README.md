# student-api

Spring Boot REST API — бэкенд личного кабинета студента.

## Стек

- Java 8, Spring Boot 2.7
- Spring Security (JWT, stateless)
- Spring Data JPA + Hibernate
- Flyway (миграции БД)
- H2 (demo) / PostgreSQL (prod)
- Maven

## Запуск

Перед запуском поднять `mock-1c` (см. `mock-1c/README.md`).

```bash
# Demo-режим: H2 in-memory, данные сбрасываются при перезапуске
mvn -Dspring.profiles.active=demo spring-boot:run

# PostgreSQL: данные сохраняются
docker compose up -d postgres   # из корня репо
mvn -Dspring.profiles.active=postgres spring-boot:run
```

API доступно на `http://localhost:8080`.

## Тесты и сборка

```bash
mvn test       # запустить все тесты
mvn package    # собрать JAR
```

## API

```
POST /api/auth/login                  — получить JWT-токен
GET  /api/student/profile             — профиль студента
GET  /api/student/grades              — оценки
GET  /api/student/grades/history      — история изменений оценок (требует Kafka)
GET  /api/student/schedule            — расписание
GET  /api/student/debts               — задолженности
GET  /actuator/health                 — статус приложения
GET  /h2-console                      — консоль H2 (только demo-профиль)
```

Все `/api/student/*` эндпоинты требуют заголовок `Authorization: Bearer <token>`.

## Структура пакетов

```
ru.university.studentapi
├── controller/     REST-контроллеры + обработчик ошибок
├── service/        бизнес-логика (StudentCabinetService, AuthService, OneCSyncService)
├── repository/     JPA-репозитории
├── entity/         JPA-сущности
├── dto/            DTO для API
├── security/       JwtService, JwtAuthenticationFilter, SecurityConfig
├── integration/    OneCStudentDataProvider (HTTP-клиент к 1С / mock-1c)
└── event/          Kafka-события изменения оценок (опционально)
```

## Профили конфигурации

| Профиль | БД | Описание |
|---|---|---|
| `demo` | H2 in-memory | Для локальной разработки, данные сбрасываются при перезапуске |
| `postgres` | PostgreSQL | Для запуска с постоянным хранением данных |

Переменные окружения: `JWT_SECRET`, `JWT_TTL_MINUTES`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `KAFKA_ENABLED`, `ONEC_BASE_URL`. Значения по умолчанию — в `application.yml` и профильных yml-файлах.
