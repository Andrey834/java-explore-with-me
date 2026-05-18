# Explore With Me

Социальная платформа для создания и поиска событий. Пользователи могут публиковать мероприятия, регистрироваться на чужие, а администраторы — модерировать контент. Сервис статистики фиксирует просмотры и помогает ранжировать события по популярности.

## Архитектура

```
┌────────────────────────────────────────────────────────────────────┐
│                          Docker Network                            │
│                                                                    │
│   ┌──────────────┐     ┌───────────────┐     ┌────────────────┐   │
│   │   ewm-main   │────▶│ stat-gateway  │────▶│  stat-server   │   │
│   │  :8080       │     │  :9091        │     │  :9090         │   │
│   └──────┬───────┘     └───────────────┘     └───────┬────────┘   │
│          │                                           │            │
│   ┌──────▼───────┐                         ┌────────▼───────┐    │
│   │    ewm-db    │                         │    stats-db    │    │
│   │  PostgreSQL  │                         │  PostgreSQL    │    │
│   └──────────────┘                         └────────────────┘    │
└────────────────────────────────────────────────────────────────────┘
```

| Модуль | Описание |
|---|---|
| `ewm-service/ewm-main` | Основной REST-сервис: события, категории, пользователи, локации |
| `ewm-service/ewm-dto` | Общие DTO, используемые основным сервисом |
| `stat-service/stat-gateway` | Gateway для сервиса статистики: кеширование, маршрутизация |
| `stat-service/stat-server` | Хранение и агрегация статистики просмотров |

## Стек технологий

- **Java 11** + **Spring Boot 2.7**
- **Spring Data JPA** / **Hibernate**
- **PostgreSQL 16**
- **Lombok**
- **Docker** / **Docker Compose**
- **Yandex Maps Geocoder API** — геокодирование адресов

## Быстрый старт

### Предварительные требования

- Docker и Docker Compose
- Ключ Yandex Maps Geocoder API ([получить](https://developer.tech.yandex.ru/))

### Запуск

```bash
# 1. Клонируйте репозиторий
git clone https://github.com/Andrey834/java-explore-with-me.git
cd java-explore-with-me

# 2. Создайте файл окружения
cp .env.example .env
# Откройте .env и укажите свой YANDEX_MAP_API_KEY

# 3. Соберите и запустите все сервисы
docker-compose up --build
```

После запуска:
- Основной сервис: `http://localhost:8080`
- Stat Gateway: `http://localhost:9091`
- Stat Server: `http://localhost:9090`

## Переменные окружения

| Переменная | Описание | Пример |
|---|---|---|
| `YANDEX_MAP_API_KEY` | Ключ API Яндекс Карт для геокодирования | `abc123...` |

Порты, URL баз данных и пароли конфигурируются в `docker-compose.yml`.

## API

Полная спецификация OpenAPI доступна в файлах:
- [`ewm-main-service-spec.json`](ewm-main-service-spec.json) — основной сервис
- [`ewm-stats-service-spec.json`](ewm-stats-service-spec.json) — сервис статистики

### Основные эндпоинты

#### Публичный доступ

| Метод | Путь | Описание |
|---|---|---|
| `GET` | `/events` | Список опубликованных событий с фильтрацией |
| `GET` | `/events/{id}` | Информация о конкретном событии |
| `GET` | `/categories` | Список категорий |
| `GET` | `/categories/{id}` | Информация о категории |
| `GET` | `/compilations` | Список подборок событий |
| `GET` | `/compilations/{id}` | Конкретная подборка |

#### Приватный доступ (авторизованный пользователь)

| Метод | Путь | Описание |
|---|---|---|
| `GET` | `/users/{userId}/events` | Мои события |
| `POST` | `/users/{userId}/events` | Создать событие |
| `GET` | `/users/{userId}/events/{eventId}` | Моё событие |
| `PATCH` | `/users/{userId}/events/{eventId}` | Редактировать событие |
| `GET` | `/users/{userId}/events/{eventId}/requests` | Заявки на участие |
| `PATCH` | `/users/{userId}/events/{eventId}/requests` | Подтвердить / отклонить заявки |
| `GET` | `/users/{userId}/requests` | Мои заявки на участие |
| `POST` | `/users/{userId}/requests` | Подать заявку |
| `PATCH` | `/users/{userId}/requests/{requestId}/cancel` | Отменить заявку |

#### Административный доступ

| Метод | Путь | Описание |
|---|---|---|
| `GET` | `/admin/events` | Поиск событий с фильтрами |
| `PATCH` | `/admin/events/{eventId}` | Публикация / отклонение события |
| `GET/POST/PATCH/DELETE` | `/admin/categories` | Управление категориями |
| `GET/POST/PATCH/DELETE` | `/admin/compilations` | Управление подборками |
| `GET/POST/DELETE` | `/admin/users` | Управление пользователями |
| `GET/POST/PATCH/DELETE` | `/admin/locations` | Управление локациями |

### Фильтрация событий по геолокации

Помимо стандартных фильтров, эндпоинты `GET /events` и `GET /admin/events` поддерживают параметр `address`. При его передаче сервис геокодирует адрес через Яндекс Карты и возвращает только события, чья локация попадает в заданный радиус.

### Сервис статистики

| Метод | Путь | Описание |
|---|---|---|
| `POST` | `/hit` | Зафиксировать просмотр эндпоинта |
| `GET` | `/stats` | Получить статистику просмотров |

Параметры `/stats`:
- `start`, `end` — временной диапазон (`yyyy-MM-dd HH:mm:ss`)
- `uris` — список URI для фильтрации (опционально)
- `unique` — считать только уникальные IP (по умолчанию `false`)

## Жизненный цикл события

```
PENDING ──▶ PUBLISHED
   │
   └──▶ CANCELED
```

- Пользователь создаёт событие → статус `PENDING`
- Администратор публикует (`PUBLISH_EVENT`) → `PUBLISHED`
- Администратор отклоняет (`REJECT_EVENT`) → `CANCELED`
- Пользователь может отменить своё событие → `CANCELED`

## Модерация заявок на участие

Если для события включена модерация (`requestModeration = true`), поступающие заявки имеют статус `PENDING` до решения организатора. При отключённой модерации или нулевом лимите участников заявки подтверждаются автоматически.

## Структура проекта

```
java-explore-with-me/
├── ewm-service/
│   ├── ewm-dto/          # Общие DTO
│   └── ewm-main/         # Основной сервис
│       └── src/main/java/ru/practicum/ewmmain/
│           ├── controller/   # REST-контроллеры (admin / private / public)
│           ├── service/      # Бизнес-логика
│           ├── repository/   # JPA-репозитории
│           ├── model/        # JPA-сущности
│           ├── mapper/       # DTO ↔ Entity
│           ├── enums/        # StateEvent, StateAction и др.
│           ├── exception/    # Доменные исключения
│           └── client/       # HTTP-клиент для stat-gateway
├── stat-service/
│   ├── stat-gateway/     # API Gateway с кешированием
│   └── stat-server/      # Сервис хранения статистики
├── docker-compose.yml
├── .env.example
└── pom.xml
```
