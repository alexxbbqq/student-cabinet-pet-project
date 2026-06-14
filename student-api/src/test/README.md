# Тесты student-api

## Запуск

Все тесты сразу:
```bash
cd student-api
mvn test
```

Один конкретный класс:
```bash
mvn test -Dtest=JwtServiceTest
mvn test -Dtest=AuthServiceTest
mvn test -Dtest=StudentCabinetServiceTest
mvn test -Dtest=StudentControllerIntegrationTest
```

## Что есть

| Класс | Тип | Описание |
|-------|-----|----------|
| `JwtServiceTest` | Unit | Создание токена, валидация, истёкший/поддельный токен |
| `AuthServiceTest` | Unit | Логин с верными/неверными/отсутствующими credentials |
| `StudentCabinetServiceTest` | Unit | getProfile/getGrades/getSchedule/getDebts, отсутствие студента |
| `StudentControllerIntegrationTest` | Integration | 401 без токена, 200 с токеном, полный Spring-контекст |

## Требования

- JDK 8+ (`JAVA_HOME` должен указывать на JDK, не JRE)
- Maven в PATH
- Внешние сервисы не нужны — тесты используют H2 и mock-данные
