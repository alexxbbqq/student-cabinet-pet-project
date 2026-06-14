# student-cabinet

React + Vite фронтенд личного кабинета студента.

## Стек

- React 18
- Vite
- CSS (переменные, без фреймворков)

## Запуск

```bash
npm install
npm run dev
```

Приложение открывается на `http://localhost:5173`. Бэкенд (`student-api`) должен быть запущен на `http://localhost:8080`.

Если браузер не открывается из-за `ERR_CONNECTION_REFUSED`:

```bash
npm run dev -- --host
```

## Демо-вход

```
Логин:  2021-301-047
Пароль: password
```

## Структура

Всё приложение — один файл [`src/App.jsx`](src/App.jsx):

| Компонент | Описание |
|---|---|
| `LoginPage` | Страница входа |
| `AppShell` | Оболочка с сайдбаром и навигацией |
| `GradesPage` | Оценки: таблица, фильтры, статистика |
| `SchedulePage` | Расписание по дням |
| `DebtsPage` | Академические задолженности |
| `ProfilePage` | Профиль студента |

Стили — [`src/index.css`](src/index.css). Цвета через CSS-переменные (`--brand`, `--danger`, `--good` и др.).

## Конфигурация

Адрес бэкенда задаётся переменной окружения:

```env
VITE_API_BASE_URL=http://localhost:8080
```

Скопировать из `.env.example` и при необходимости изменить.
