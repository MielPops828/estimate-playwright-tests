# Estimate App — Playwright Tests

Автоматизированное тестирование веб-приложения **Estimate App** с использованием Java и Playwright.

Проект содержит UI- и API-автотесты, Page Object Model, управление тестовыми данными, интеграцию с Allure и возможность запуска тестов в Docker.

Estimate App — внутренний веб-сервис для оценки проектов и планирования трудозатрат. Система позволяет создавать и редактировать оценки проектов, указывать информацию о проекте и ответственных сотрудниках, формировать фазы и задачи, а также рассчитывать и контролировать трудозатраты.

## Стек

- Java 8+
- Maven
- Playwright 1.63.0
- TestNG 7.9.0
- Allure 2.33.0
- Jackson
- Lombok
- Docker
- Selenoid

## Структура проекта

```text
tests/
├── configuration/
│   ├── aut_default.xml
│   ├── env_docker_selenoid.xml
│   ├── env_local.xml
│   └── tests_default.xml
│
├── src/
│   └── test/
│       ├── java/
│       │   ├── api/
│       │   │   ├── models/
│       │   │   ├── tests/
│       │   │   ├── utills/
│       │   │   └── TestEstimateAPI.java
│       │   │
│       │   ├── helpers/
│       │   │   ├── EstimateCreator.java
│       │   │   └── ParametersProvider.java
│       │   │
│       │   └── ui/
│       │       ├── pages/
│       │       └── tests/
│       │
│       └── resources/
│           └── allure.properties
│
├── Dockerfile
├── docker-run.bat
└── pom.xml

docker-compose.yml
browsers.json
```

## Что тестируется

### UI

Автотесты покрывают основные сценарии работы с оценками проекта:

- авторизация с корректными данными;
- авторизация с некорректными данными;
- авторизация с пустым логином;
- авторизация с пустым паролем;
- создание оценки;
- проверка созданной оценки;
- удаление оценки;
- добавление пользовательской фазы;
- удаление фазы.

Для UI-тестов используется **Page Object Model**.

Основные Page Object:

- `LoginPage`
- `EstimatesPage`
- `EstimateEditPage`
- `EstimateDetailPage`
- `EstimatePhaseComponent`
- `EstimateMenuComponent`

Общая настройка браузера и жизненный цикл Playwright находятся в `BaseTest`.

### API

API-тесты работают с REST API приложения и покрывают:

- создание оценки;
- получение оценки;
- копирование оценки;
- удаление оценки;
- проверку HTTP-статусов;
- проверку тела ответа;
- проверку идентификаторов и статусов оценки.

Для работы с API используется встроенный в Playwright `APIRequestContext`.

Авторизация выполняется через:

```text
POST /api/auth/login
```

После получения токена он используется для последующих API-запросов.

## Архитектура тестов

### UI

```text
Test
 │
 ├── BaseTest
 │    ├── Playwright
 │    ├── Browser
 │    ├── BrowserContext
 │    └── Page
 │
 ├── Page Objects
 │    ├── LoginPage
 │    ├── EstimatesPage
 │    ├── EstimateEditPage
 │    ├── EstimateDetailPage
 │    └── EstimatePhaseComponent
 │
 └── Helpers
      └── EstimateCreator
```

### API

```text
EstimationTest
      │
      └── TestEstimateAPI
             │
             ├── APIRequestContext
             ├── authentication
             ├── GET
             ├── POST
             └── DELETE
```

Для десериализации JSON-ответов используются DTO-модели:

```text
EstimateRequest
EstimateResponse
LoginRequest
```

## Управление тестовыми данными

Конфигурация разделена на:

- `aut_default.xml` — тестовые данные приложения;
- `env_local.xml` — настройки локального запуска;
- `env_docker_selenoid.xml` — настройки запуска в Docker/Selenoid.

Параметры загружаются через `ParametersProvider`.

Это позволяет отделить настройки окружения и тестовые данные от тестовой логики.

## Очистка тестовых данных

Тесты не оставляют созданные оценки после выполнения.

В UI-тестах созданные оценки сохраняются в список `createdEstimateHrefs` и удаляются после теста.

В API-тестах аналогичный механизм реализован через список идентификаторов созданных оценок:

```java
estimatesId
```

После выполнения теста созданные сущности удаляются через API.

## Allure

В проект интегрирован Allure для формирования отчётов.

Используются:

- `@Epic`
- `@Feature`
- `@Story`
- `@Severity`
- `@Step`

Пример:

```java
@Epic("Автоматизация в приложении Estimate App")
@Feature("Создание оценки")
@Story("Успешное создание новой оценки")
```

Результаты тестов сохраняются в:

```text
target/allure-results
```

Для генерации отчёта используется Maven Allure Plugin.

## Конфигурация запуска

В проекте предусмотрены отдельные конфигурации для различных окружений.

### Локальный запуск

Используется:

```text
env_local.xml
```

Приложение:

```text
http://localhost:8001
```

### Docker / Selenoid

Для Docker используется:

```text
env_docker_selenoid.xml
```

Selenoid доступен по адресу:

```text
http://selenoid:4444/wd/hub
```

Также в проекте присутствуют:

```text
docker-compose.yml
browsers.json
```

для настройки Selenoid и браузеров.

## Запуск тестов

Перейти в директорию `tests`:

```bash
cd tests
```

Запустить тесты Maven:

```bash
mvn clean test
```

Для запуска в headless-режиме:

```bash
mvn clean test -Dheadless=true
```

Для запуска с указанным URL приложения:

```bash
mvn clean test -DwebUrl=http://localhost:8001
```

## Запуск в Docker

Для Windows предусмотрен скрипт:

```text
tests/docker-run.bat
```

Он выполняет:

1. сборку Docker image;
2. запуск контейнера с тестами;
3. передачу URL приложения;
4. выполнение Maven-тестов;
5. генерацию Allure-отчёта.

Также в проекте присутствует `Dockerfile` на базе официального Playwright Java image.

## TestNG

Тестовый suite находится в:

```text
tests/configuration/tests_default.xml
```

В него входят:

- `LoginTest`
- `EstimateTest`
- `EstimationTest`

UI и API-тесты разделены по пакетам:

```text
ui.tests
api.tests
```

## Особенности проекта

В проекте реализованы:

- UI-автоматизация на Playwright;
- API-автоматизация через Playwright API;
- Page Object Model;
- DTO для API;
- автоматическая авторизация;
- переиспользуемые helper-классы;
- параметризация через конфигурационные файлы;
- автоматическая очистка тестовых данных;
- Soft Assertions;
- Allure-отчётность;
- запуск в headless-режиме;
- Docker-окружение;
- интеграция с Selenoid.

## Цель проекта

Проект разработан для практики автоматизации тестирования веб-приложения **Estimate App** и демонстрирует построение тестового фреймворка на Java + Playwright с поддержкой UI-, API- и контейнерного запуска.