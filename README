# Album Rating Tracker

A full-stack web application for logging and rating music albums using a custom
weighted-scoring system. Replaces a personal manual tracking process (spreadsheet)
with a proper tool — built as a portfolio project for internship applications.

## Overview

Users add albums and rate them across six categories:

- Songwriting
- Production
- Cohesion
- Tracklist Quality
- Replay Value
- Emotional Impact

Each category has a **user-adjustable weight**, and the app auto-calculates a
final weighted score per album. Albums can be browsed, filtered, and sorted by
score, artist, genre, or date added.

## Tech Stack

| Layer     | Technology                          |
|-----------|--------------------------------------|
| Backend   | Spring Boot (Java), Maven            |
| Database  | PostgreSQL, Flyway migrations        |
| Frontend  | React                                |
| Auth      | JWT-based authentication             |
| DevOps    | Docker Compose (local dev), GitHub Actions (CI/CD) |
| Hosting   | Render / Fly.io (free tier)          |

## Data Model

![ER Diagram](docs/er-diagram.png)

- **app_user** — registered users
- **scoring_weights** — one-to-one with user, holds the six adjustable category weights
- **album** — belongs to a user, holds the six raw category scores; weighted total is computed at query time

## Features (MVP)

- [x] Database schema design
- [ ] Add / edit albums with scores across six weighted categories
- [ ] Auto-calculated weighted total score
- [ ] Browse / filter / sort rated albums (by score, artist, genre, date)
- [ ] JWT authentication

## Stretch Goals

- Genre / artist search
- Stats dashboard (average score by genre, most-rated artists)
- Live deployed demo

## Local Development

### Prerequisites

- Java 21+
- Maven
- Node.js + npm
- Docker

### Backend

\`\`\`bash
docker compose up -d       # starts Postgres
cd backend
./mvnw spring-boot:run     # Flyway runs migrations automatically
\`\`\`

### Frontend

\`\`\`bash
cd frontend
npm install
npm start
\`\`\`

## Status

🚧 Early development — schema and backend scaffolding in progress.