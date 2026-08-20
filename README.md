# Auction REST Server

Projet personnel d'API REST d'enchères en ligne, développé pour approfondir les compétences en développement backend, tests et DevOps.

[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue)](https://docs.docker.com/compose/)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/Max-Rougeux/auction-spring-api)
---

## Client Angular

Interface web du projet, développée en Angular 21 avec une architecture basée sur les Signals (state réactif) et des mises à jour temps réel via WebSocket (STOMP).

[![Angular](https://img.shields.io/badge/Angular-21-DD0031?logo=angular&logoColor=white)](https://github.com/Max-Rougeux/auction-angular-client)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/Max-Rougeux/auction-angular-client)
---

## Stack technique

| Couche | Technologies |
|---|---|
| Framework | Spring Boot 4.0.6, Spring Web MVC |
| Sécurité | Spring Security, OAuth2 Resource Server, JWT (HS256) |
| Persistance | Spring Data JDBC (SQL Server), Spring Data JPA / Hibernate (PostgreSQL), Mock in-memory |
| Temps réel | WebSocket (STOMP over SockJS) |
| Documentation | SpringDoc OpenAPI 3 (Swagger UI) |
| Build | Maven |
| Java | 21 |

---

## Prérequis

- Java 21
- Maven 3.x+
- Docker & Docker Compose _(requis pour les profils `sqlserver` et `postgresql`)_

---

## Configuration

Copier `.env.example` en `.env` à la racine du projet et renseigner les valeurs :

```env
# App Settings
LOG_LEVEL=INFO
AUCTION_PORT=8080

# Containers
CONTAINER_API=auction-api
CONTAINER_SQLSERVER=auction-sqlserver
CONTAINER_POSTGRESQL=auction-postgresql

# SQL Server
MS_DB_HOST=localhost
MS_DB_PORT=1433
MS_DB_USERNAME=sa
MS_DB_PASSWORD=your_strong_password

# PostgreSQL
PG_DB_HOST=localhost
PG_DB_PORT=5432
PG_DB_USERNAME=postgres
PG_DB_PASSWORD=your_strong_password

# JWT Secret (min. 32 bytes hex)
JWT_SECRET=your_secret_key_here

# Admin Account
ADMIN_USERNAME=your_admin_email
ADMIN_PASSWORD=your_admin_password
```

---

## Profils disponibles

| Profil | Persistance | Couche d'accès | Dépendances externes | État |
|---|---|---|---|---|
| `mock` | In-memory (ArrayList) | DAO custom | Aucune | ✅ Stable |
| `sqlserver` | Microsoft SQL Server | Spring Data JDBC | Docker |  ✅ Stable |
| `postgresql` | PostgreSQL | Spring Data JPA / Hibernate | Docker |  ✅ Stable |

Le profil actif se règle via la variable `SPRING_PROFILES_ACTIVE` (défaut : `mock`).

---

## Endpoints REST

Toutes les réponses suivent l'enveloppe `ApiResponse<T>` (`code`, `message`, `data`, `meta` optionnel avec pagination `page`/`size`/`total`/`pages`).

| Méthode | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/auth/login` | Non | Authentification, retourne JWT + refresh token cookie |
| `POST` | `/api/auth/refresh` | Non | Renouvelle le JWT via le cookie refresh token |
| `POST` | `/api/auth/logout` | Non | Révoque le refresh token et expire le cookie |
| `GET` | `/api/me` | Oui | Profil de l'utilisateur courant |
| `GET` | `/api/users` | Oui (`ROLE_ADMIN`) | Liste paginée des utilisateurs |
| `GET` | `/api/users/{slug}` | Oui | Détail utilisateur (résumé public) |
| `GET` | `/api/categories` | Oui | Liste des catégories avec comptage |
| `GET` | `/api/sales` | Oui | Liste paginée des ventes (filtre `category` optionnel) |
| `GET` | `/api/sales/{slug}` | Oui | Détail d'une vente |
| `GET` | `/api/bids/{slug}` | Oui | Liste paginée des enchères d'une vente |
| `GET` | `/api/bids/latest` | Oui | Dernières enchères tous lots confondus |
| `GET` | `/api/bids/{slug}/all` | Oui | Données du graphique d'évolution des enchères |
| `POST` | `/api/bids/place` | Oui | Soumettre une enchère |

### Swagger UI

Accessible à `http://localhost:8080/` une fois l'application démarrée.

---

## Endpoints WebSocket (STOMP)

La connexion s'établit sur `/ws`. L'authentification se fait via le JWT transmis dans les headers STOMP à la connexion.

### Topics publics (subscribe)

| Destination | Description |
|---|---|
| `/topic/live-bids` | Diffuse toutes les nouvelles enchères en temps réel |
| `/topic/live-bids/{slug}` | Diffuse les nouvelles enchères pour une vente spécifique |
| `/topic/sales/price` | Diffuse les mises à jour de prix courant des ventes |

### Queue utilisateur (subscribe, auth requise)

| Destination | Description |
|---|---|
| `/user/queue/credit` | Notifie l'utilisateur surenchéri de son remboursement (`refund`, `slug`, `amount`) |

---

## Authentification

L'API utilise JWT (HS256) avec un système de refresh token rotatif :

- Le JWT a une durée de vie de **15 minutes**
- Le refresh token est stocké en base et transmis via cookie **HttpOnly** (durée 7 jours, path `/api/auth`)
- À chaque refresh, l'ancien token est révoqué et un nouveau est généré
- Pour les WebSockets, le JWT doit être transmis dans les **headers STOMP** à la connexion

---

## Structure du projet

```
auction-rest/
├── docker/
│   ├── api/
│   │   └── Dockerfile                      # Image Spring Boot
│   └── sqlserver/
│       ├── Dockerfile
│       ├── entrypoint.sh
│       └── init.sql                        # Script init BDD
├── scripts/
│   ├── run.sh                              # Script de lancement Linux/macOS
│   └── run.bat                             # Script de lancement Windows
├── src/
│   ├── main/
│   │   ├── java/.../auction/
│   │   │   ├── AuctionApplication.java
│   │   │   ├── configuration/
│   │   │   │   ├── data/                   # JpaDataSourceConfig, SqlDataSourceConfig
│   │   │   │   ├── security/               # SpringSecurityConfig, JwtChannelInterceptor, entry/handler
│   │   │   │   └── web/                    # CorsConfig, LocaleConfig, StompConfig
│   │   │   ├── controller/
│   │   │   │   ├── rest/                   # SaleRestController, BidRestController, UserRestController...
│   │   │   │   │   └── action/             # AuthRestController, BiddingRestController
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── dal/                        # Interfaces DAO (BidDao, SaleDao, UserDao...)
│   │   │   │   ├── jpa/                    # Impl PostgreSQL : entity, mapper, repository, converter
│   │   │   │   ├── sqlserver/              # Impl SQL Server : DAO JDBC + RowMapper
│   │   │   │   └── mock/                   # Impl in-memory : batch, loader, storage
│   │   │   ├── domain/
│   │   │   │   ├── aggregate/              # BiddingAggregate
│   │   │   │   ├── bo/                     # Objets métier (User, Sale, Bid, Item...)
│   │   │   │   ├── enums/                  # Condition, ImageType, Role, State
│   │   │   │   └── projection/             # Projections composites (SaleProjection...)
│   │   │   ├── event/                      # BidPlacedEvent + listener/
│   │   │   ├── exception/                  # Exceptions métier
│   │   │   ├── fixtures/                   # DataInitializer + initializers/ (seed)
│   │   │   ├── mapper/                     # Mappers BO <-> DTO web
│   │   │   ├── service/                    # Logique métier
│   │   │   │   ├── action/                 # AuthService, BiddingService, CreditService
│   │   │   │   ├── cache/                  # MeCache, SaleCache
│   │   │   │   └── security/               # AccessTokenService, RefreshTokenService...
│   │   │   ├── support/                    # ApiCodes, I18nHelper, factory/ (ApiResponseFactory, CookieFactory)
│   │   │   ├── utils/                      # SlugUtils
│   │   │   └── web/                        # DTOs REST/WS : api, bid, category, image, item, request, response, result, sale, user, ws
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── messages.properties
│   │       └── messages_en.properties
│   └── test/
│       └── java/.../auction/
│           └── AuctionApplicationTests.java
├── uploads/                                # Fichiers uploadés (volume Docker)
│   ├── user/
│   └── item/
├── .env.example
├── local.yml                               # Docker Compose dev
└── production.yml                          # Docker Compose prod
```
