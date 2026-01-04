# TaskManager

Проект состоит из трёх микросервисов и фронтенда.  
Можно запускать локально через Gradle/npm (dev) или через Docker (prod/локальный прод).

## 🚀 Quick Start

Для быстрого запуска проекта локально (Postgres, Kafka, микросервисы и фронт) можно выполнить следующие команды:

# 1️⃣ Поднять Postgres и Kafka через Docker
```
docker-compose up -d postgres kafka zookeeper
```

# 2️⃣ Запустить все микросервисы через Gradle
```
./gradlew runAll
```

# 3️⃣ Перейти в фронтенд и запустить dev-сервер
```
cd frontend
npm install
npm run dev
```

## Структура проекта

```
TaskManager/
├─ user-service/
├─ task-service/
├─ project-service/
├─ frontend/
├─ .env
├─ docker-compose.yml
├─ build.gradle.kts
└─ settings.gradle.kts
```

- **.env** — все переменные окружения для продакшн запуска.
- **.env.dev** — переменные окружения для локальной разработки.
- **docker-compose.yml** — поднимает Postgres, Kafka, Zookeeper и все сервисы в Docker.
- **frontend/** — фронтенд на React/Vue/Angular (любой npm фронт).
- **user-service/**, **task-service/**, **project-service/** — бэкенд на Spring Boot.

---

## 1️⃣ Настройка переменных окружения

Создай `.env.dev` в корне проекта:

```env
# === Postgres ===
POSTGRES_DB=taskmanager
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_db_password
POSTGRES_HOST=localhost
POSTGRES_PORT=5432

# === Kafka ===
KAFKA_HOST=localhost
KAFKA_PORT=9092

# === Сервисы ===
USER_SERVICE_PORT=8081
TASK_SERVICE_PORT=8082
PROJECT_SERVICE_PORT=8083
```

Эти переменные используются как для Docker, так и для локального запуска через Gradle.

2️⃣ Локальная разработка
Поднять Postgres и Kafka через Docker:

docker compose --env-file .env.dev up

Запустить микросервисы через Gradle:

```
# В корне проекта
$env:SPRING_PROFILES_ACTIVE="dev"; ./gradlew :user-service:bootRun
$env:SPRING_PROFILES_ACTIVE="dev"; ./gradlew :task-service:bootRun
$env:SPRING_PROFILES_ACTIVE="dev"; ./gradlew :project-service:bootRun
```
 
Или единым таском:

```
./gradlew runAll
Профиль dev использует application-dev.yml.
```

Liquibase отключён (enabled: false).

Hibernate ddl-auto: update.

Порты берутся из .env.dev.

Frontend
Перейти в папку фронтенда:

```
cd frontend
```
Установить зависимости:

```
npm install
```
Запустить dev-сервер с горячей перезагрузкой:

```
npm run dev
```
Фронтенд запускается на http://localhost:5173.

API микросервисов доступны по портам из .env.dev (8081-8083).

3️⃣ Продакшн запуск через Docker
Собрать сервисы:

```
./gradlew clean build -x test
```
Поднять все контейнеры:

```
docker compose -f docker-compose.yml -f docker-compose.prod.yml --env-file .env.prod up -d
```
Профиль Spring: prod (SPRING_PROFILES_ACTIVE=prod).

Liquibase включён только для project-service, который управляет миграциями.

Kafka, Postgres и Zookeeper поднимаются через Docker.

Фронт можно деплоить отдельно, без пересборки бэка.

4️⃣ Проверка
Микросервисы:

User Service: http://localhost:8081

Task Service: http://localhost:8082

Project Service: http://localhost:8083

Фронтенд: http://localhost:5173

Kafka: localhost:9092

Postgres: localhost:5432

5️⃣ Полезные команды

# Остановить все Docker-сервисы
```
docker-compose down
```
# Смотреть логи микросервиса
```
docker-compose logs -f user-service
```

# Пересобрать и поднять заново
```
docker-compose up -d --build
```

6️⃣ Советы
Для локальной разработки фронт и бэки лучше держать раздельно, чтобы не пересобирать Docker каждый раз.

Для прод/стейджинга удобно упаковывать всё в Docker, включая фронт (через Nginx или статичные файлы).

Профили dev и prod управляют Liquibase, Hibernate и логированием.
