# Личный кабинет студента РГУ им. А.Н. Косыгина

Учебный Java-проект личного кабинета студента. Проект показывает бэкенд на Spring Boot, работу с БД, JWT-авторизацию, синхронизацию учебных данных из внешнего источника и небольшой web-интерфейс на React.

В локальном режиме внешний источник данных имитируется сервисом `mock-1c`. Бэкенд забирает из него профиль, оценки, расписание и задолженности, сохраняет данные в свою БД, а фронтенд получает их уже через REST API.

## Быстрый старт

**Вариант 1 — Docker (рекомендуется):**

```bash
docker compose up --build
```

Поднимает весь стек одной командой. Приложение будет доступно на `http://localhost`.

**Вариант 2 — локально (три отдельных терминала):**

```powershell
.\start.ps1
```

Открывает три окна: mock-1c, бэкенд (demo/H2) и фронтенд. Приложение будет доступно на `http://localhost:5173`.

## Что реализовано

- вход студента по логину и паролю;
- выдача JWT-токена;
- защищенные эндпоинты личного кабинета;
- профиль студента;
- оценки;
- расписание;
- академические задолженности;
- пользователи приложения в таблице `app_users`;
- BCrypt-хеширование паролей;
- миграции БД через Flyway;
- H2 demo-режим для локального запуска;
- PostgreSQL для постоянного хранения локальной копии данных;
- Docker Compose для запуска PostgreSQL;
- mock-сервис внешнего контура 1С;
- синхронизация данных из mock-1C в БД приложения.

## Стек

Бэкенд:

- Java 8+
- Spring Boot 2.7
- Spring Security
- Spring Data JPA
- Hibernate
- Flyway
- Maven

База данных:

- H2 для локального demo-режима
- PostgreSQL для реального развертывания

Фронтенд:

- React
- Vite
- CSS

Mock-1C:

- Node.js HTTP server без внешних зависимостей

## Структура

```text
project_rguk/
  mock-1c/           mock-сервис внешнего источника данных
  student-api/       Spring Boot бэкенд
  student-cabinet/   React + Vite фронтенд
```

## Архитектура

Синхронизация данных:

```text
mock-1c HTTP service
  -> OneCStudentDataProvider
    -> OneCSyncService
      -> H2 или PostgreSQL
```

Работа личного кабинета:

```text
React фронтенд
  -> Spring Boot REST API
    -> JwtAuthenticationFilter
      -> StudentController
        -> StudentCabinetService
          -> JPA repositories
            -> H2 или PostgreSQL
```

Фронтенд не обращается к 1С напрямую. Он работает только с `student-api`. Бэкенд читает данные кабинета из своей БД, а БД обновляется синхронизацией из внешнего источника.

## Запуск

### Docker (рекомендуется)

Требования: Docker Desktop.

```bash
docker compose up --build
```

Поднимает сразу mock-1c, PostgreSQL, бэкенд и фронтенд. При первом запуске Flyway создаст таблицы и `OneCSyncService` загрузит демо-данные из mock-1C.

| Сервис | Адрес |
|---|---|
| Приложение | http://localhost |
| Backend API | http://localhost:8080 |
| mock-1C admin | http://localhost:8091/admin |

### Локально (три терминала)

Требования: JDK 8+, Maven, Node.js и npm.

**Быстрый старт — одна команда (Windows):**

```powershell
.\start.ps1
```

**Или вручную:**

```bash
# Терминал 1
cd mock-1c && node server.js

# Терминал 2 — H2 demo (данные сбрасываются при перезапуске)
cd student-api && mvn -Dspring-boot.run.profiles=demo spring-boot:run

# Терминал 3
cd student-cabinet && npm install && npm run dev
```

Приложение: `http://localhost:5173`

**С PostgreSQL** (данные сохраняются между перезапусками):

```bash
docker compose up -d postgres
cd student-api && mvn -Dspring-boot.run.profiles=postgres spring-boot:run
```

При первом запуске Flyway создаст таблицы, после чего `OneCSyncService` загрузит демо-студентов из mock-1C в PostgreSQL. Дальше API будет отдавать данные из PostgreSQL, а не ходить в mock-1C на каждый запрос фронтенда.

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

## Проверка сервисов

Фронтенд:

```text
http://localhost:5173
```

Проверка бэкенд:

```text
http://localhost:8080/actuator/health
```

Mock-1C:

```text
http://localhost:8091/onec/students/2021-301-047/grades
```

H2 console:

```text
http://localhost:8080/h2-console
```

JDBC URL для demo-режима:

```text
jdbc:h2:mem:student_cabinet
```

## API

Авторизация:

```text
POST /api/auth/login
```

Эндпоинты студента:

```text
GET /api/student/profile
GET /api/student/grades
GET /api/student/grades/history
GET /api/student/schedule
GET /api/student/debts
```

Защищенные эндпоинты требуют заголовок:

```http
Authorization: Bearer <token>
```

## База данных

Flyway создает таблицы:

- `students`;
- `grades`;
- `schedule_days`;
- `lessons`;
- `debts`;
- `app_users`;
- `grade_audit_log`.

В demo-режиме используется H2 in-memory БД. Без demo-профиля бэкенд ожидает PostgreSQL:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/student_cabinet}
    username: ${DB_USERNAME:student_app}
    password: ${DB_PASSWORD:student_app}
```

Для локального PostgreSQL можно использовать значения по умолчанию из `.env.example`:

```text
DB_URL=jdbc:postgresql://localhost:5432/student_cabinet
DB_USERNAME=student_app
DB_PASSWORD=student_app
```

## Интеграция с 1С

Реальная 1С в проект не входит. Для локальной разработки используется `mock-1c`, который имитирует внешний HTTP-источник учебных данных.

В demo-профиле бэкенд работает в режиме:

```yaml
app:
  integration:
    onec:
      mode: http
      base-url: http://127.0.0.1:8091
      sync-enabled: true
      sync-fixed-delay-ms: 600000
```

При старте `OneCSyncService` забирает данные из `mock-1c` и сохраняет их в БД приложения. Дальше синхронизация повторяется по расписанию.

Частота обновления задается параметром:

```text
ONEC_SYNC_FIXED_DELAY_MS=600000
```

`600000` миллисекунд означает обновление примерно раз в 10 минут. Запросы фронтенда при этом идут только в `student-api`, а `student-api` читает уже сохраненные данные из БД.

Для подключения реальной 1С нужно уточнить:

- доступный протокол: HTTP-сервис, OData, SOAP, файловый обмен или прямой доступ к БД;
- адрес тестового контура;
- способ авторизации;
- формат данных;
- частоту обновления;
- требования к персональным данным и аудиту.

После этого можно заменить `base-url` или доработать `OneCStudentDataProvider` под реальный протокол.

### Админка mock-1C

У `mock-1c` есть веб-интерфейс для редактирования "1С"-данных без перезапуска сервиса:

```text
http://localhost:8091/admin
```

Там можно выбрать студента, добавить, изменить или удалить его оценки. Изменения сразу видны через `GET /onec/students/{id}/grades` и попадают в личный кабинет после очередной синхронизации (по умолчанию каждые `ONEC_SYNC_FIXED_DELAY_MS`) или после перезапуска `student-api`.

Кнопка "Обновить" в личном кабинете не запускает синхронизацию с 1С — она просто перечитывает уже сохраненные в БД `student-api` данные.

## Kafka: журнал изменений оценок

Опциональная (по умолчанию выключена) часть проекта — событийный журнал изменений оценок на Kafka, демонстрирующий асинхронную обработку событий.

Как это работает:

```text
OneCSyncService (при синхронизации сравнивает старые и новые оценки)
  -> GradeEventPublisher (Kafka producer, топик grade-events)
    -> GradeAuditConsumer (Kafka consumer)
      -> таблица grade_audit_log
        -> GET /api/student/grades/history
```

При каждой синхронизации `OneCSyncService` сравнивает значение и статус каждой оценки с предыдущей версией. Если оценка появилась впервые или изменилась (значение или статус), публикуется событие `GradeChangedEvent` в топик Kafka `grade-events`. `GradeAuditConsumer` читает эти события и сохраняет их в таблицу `grade_audit_log`. Студент может посмотреть историю изменений своих оценок через `GET /api/student/grades/history`.

Включение:

```bash
docker compose up -d kafka
```

```bash
KAFKA_ENABLED=true KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
  mvn -Dspring-boot.run.profiles=postgres spring-boot:run
```

Если `app.kafka.enabled=false` (значение по умолчанию), используется no-op publisher: события не публикуются, consumer не запускается, и приложение не требует запущенного Kafka вообще — это касается demo-профиля с H2.

## Безопасность

- Пароли хранятся как BCrypt-хеши.
- Демо-пользователь создается миграцией `V2__create_app_users.sql`.
- `JWT_SECRET` нужно заменить перед production-запуском.
- Реальные адреса 1С, учетные данные, `.env` файлы, дампы БД и логи нельзя коммитить.

## Что можно доработать

- добавить журнал синхронизаций из 1С;
- добавить Swagger/OpenAPI;
- добавить роли администратора и сотрудника деканата;
- добавить refresh tokens;
- добавить уведомления об изменении оценок через Kafka → Telegram-бот и email.

Технические находки и известные ограничения — в [TODO.md](TODO.md).
