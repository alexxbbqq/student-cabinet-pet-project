# Student API

Spring Boot бэкенд для личного кабинета студента.

Бэкенд отвечает за авторизацию, защищенные REST-эндпоинты, работу с БД и синхронизацию учебных данных из внешнего mock-1C сервиса.

## Запуск

Требования:

- JDK 8+;
- Maven;
- Node.js для `mock-1c`;
- Docker для запуска PostgreSQL.

### Demo-профиль с H2

Сначала запустить mock-1C:

```bash
cd ../mock-1c
node server.js
```

Затем запустить Бэкенд:

```bash
mvn -Dspring-boot.run.profiles=demo spring-boot:run
```

Бэкенд стартует на:

```text
http://localhost:8080
```

Проверка состояния:

```text
http://localhost:8080/actuator/health
```

### PostgreSQL-профиль

Запустить PostgreSQL из корня проекта:

```bash
docker compose up -d postgres
```

Запустить mock-1C:

```bash
cd ../mock-1c
node server.js
```

Запустить бэкенд:

```bash
mvn -Dspring-boot.run.profiles=postgres spring-boot:run
```

По умолчанию профиль `postgres` использует:

```text
DB_URL=jdbc:postgresql://localhost:5432/student_cabinet
DB_USERNAME=student_app
DB_PASSWORD=student_app
```

При первом запуске Flyway создаст таблицы в PostgreSQL. После старта `OneCSyncService` загрузит демо-данные из mock-1C в БД, а затем будет обновлять их по расписанию.

## Авторизация

Эндпоинт:

```text
POST /api/auth/login
```

Демо-доступ:

```text
login: 2021-301-047
password: password
```

Дополнительные demo-логины с тем же паролем:

```text
2021-301-048
2022-402-015
2020-104-033
```

Успешный вход возвращает JWT-токен. Защищенные эндпоинты требуют:

```http
Authorization: Bearer <token>
```

## Эндпоинты

```text
GET /api/student/profile
GET /api/student/grades
GET /api/student/schedule
GET /api/student/debts
```

## База данных

В demo-профиле используется H2:

```text
jdbc:h2:mem:student_cabinet
```

H2 console:

```text
http://localhost:8080/h2-console
```

В PostgreSQL-профиле используется постоянная БД:

```text
jdbc:postgresql://localhost:5432/student_cabinet
```

Flyway создает таблицы:

- `students`;
- `grades`;
- `schedule_days`;
- `lessons`;
- `debts`;
- `app_users`.

## Синхронизация с mock-1C

В demo-профиле включен HTTP-режим интеграции:

```yaml
app:
  integration:
    onec:
      mode: http
      base-url: http://127.0.0.1:8091
      sync-enabled: true
      sync-fixed-delay-ms: 600000
```

Поток данных:

```text
mock-1c
  -> OneCStudentDataProvider
    -> OneCSyncService
      -> БД приложения
        -> StudentController
```

`OneCSyncService` запускает синхронизацию при старте бэкенда и затем повторяет ее по расписанию. Эндпоинты кабинета читают данные уже из БД приложения.

Это значит, что фронтенд не вызывает 1С напрямую, а backend не ходит в 1С при каждом открытии страницы. Данные обновляются по таймеру:

```text
sync-fixed-delay-ms: 600000
```

`600000` миллисекунд — примерно 10 минут.

## Настройки для production

Для реального запуска нужно:

- заменить `JWT_SECRET`;
- подключить PostgreSQL через `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`;
- заменить mock-1C на реальный источник данных;
- не коммитить реальные адреса, логины, пароли и токены.
