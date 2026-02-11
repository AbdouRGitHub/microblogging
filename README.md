# Microblogging

## Stack Technique

- **Backend**: Java 23, Spring Boot 3.4.2
- **Base de données**: PostgreSQL 17
- **Sécurité**: Spring Security avec authentification par session
- **Gestion des sessions**: Spring Session JDBC
- **Build**: Maven
- **Containerisation**: Docker & Docker Compose

## Lancer avec Docker

```bash
docker compose up --build -d
```

L'application est accessible sur http://localhost:8080

## Voir les logs

```bash
docker compose logs -f backend
```

## Arrêter

```bash
docker compose down
```

## Arrêter et supprimer les données

```bash
docker compose down -v
```