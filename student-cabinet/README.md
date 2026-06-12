# Фронтенд личного кабинета

React + Vite фронтенд для личного кабинета студента РГУ им. А.Н. Косыгина.

Фронтенд показывает профиль, оценки, расписание и задолженности. Данные загружаются через Spring Boot бэкенд `student-api`.

## Запуск

```bash
npm install
npm run dev
```

Фронтенд стартует на:

```text
http://localhost:5173
```

По умолчанию приложение обращается к бэкенду:

```text
http://localhost:8080
```

Адрес бэкенда можно изменить через `.env`:

```bash
VITE_API_BASE_URL=http://localhost:8080
```

## Демо-доступ

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

## Как фронтенд получает данные

После входа бэкенд возвращает JWT-токен. Фронтенд сохраняет его в `localStorage` и отправляет в заголовке:

```http
Authorization: Bearer <token>
```

Используемые эндпоинты:

```text
GET /api/student/profile
GET /api/student/grades
GET /api/student/schedule
GET /api/student/debts
```

Фронтенд не обращается к mock-1C напрямую. Все данные приходят только через `student-api`.
