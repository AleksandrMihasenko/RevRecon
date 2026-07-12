# RevRecon

Detect where usage and billing don't match and explain why.

## What is this

RevRecon is a backend project exploring how billing systems fail in practice.

It focuses on a simple question:

> Why does usage not match what gets billed?

## What it does

- Ingests usage events and billing records
- Returns usage-by-metric and billed-total summaries for a customer period
- Compares expected vs actual billing
- Detects mismatches (missing, duplicated, wrong price, timing issues)
- Explains why they happen

## Why

In many systems:
- Usage is tracked separately from billing
- Pricing rules are complex
- Data is often inconsistent

As a result, companies:
- Underbill (revenue leakage)
- Overbill (customer complaints)
- Fix things manually

This project explores how to detect and understand these problems.

## Running with Docker Compose

RevRecon can be started locally with Docker Compose.

This runs:
- Spring Boot backend
- PostgreSQL database
- Persistent PostgreSQL volume

### Prerequisites

- Docker or Colima
- Docker Compose

### Environment variables

Create a local env file:

```bash
cp deploy/env/.env.example deploy/env/.env
```

Default local values are safe for development and are not production credentials.

### Start the system

From the project root:

```bash
cd deploy

docker-compose up --build
```

Backend:

```text
http://localhost:8080
```

Health check:

```text
GET http://localhost:8080/api/health
```

Expected response:

```json
{"status":"UP"}
```

PostgreSQL:

```text
localhost:5432
```

Inside the Docker Compose network the backend connects to PostgreSQL via service name:

```text
jdbc:postgresql://postgres:5432/revrecon
```

### Stop and cleanup

From the `deploy` directory:

```bash
docker-compose down -v
```

This removes:
- backend container
- postgres container
- compose network
- postgres volume

Use only for local development cleanup because it removes local database data.

## Tech Stack

- Java 21
- Spring Boot 4.0.4
- PostgreSQL
- Flyway (migrations)

## Project Structure

```
RevRecon/
├── backend/           # Spring Boot application
├── docs/
│   ├── ADR/           # Architecture Decision Records
│   ├── architecture/  # System design, patterns
│   └── domain/        # Domain glossary
└── .context/          # Project context files
```

## Status

**Phase 1: Core Model + Ingestion** — Closed

- [x] Project setup
- [x] ADR-001: Layered Architecture
- [x] Data model (Customer, Plan, UsageEvent, BillingRecord)
- [x] Ingestion endpoints
- [x] First summary read endpoint
- [x] Phase 1 controller/service tests
- [x] Local Docker Compose baseline
- [x] Lightweight health endpoint

**Phase 2: Reconciliation + Pricing** — Started

- [x] First discrepancy rule: usage exists but matching billing record is missing
- [x] TestContainers passing scenario: usage exists and matching billing record exists
- [x] Minimal per-event expected charge calculator
- [ ] Compare expected charge vs billed amount
- [ ] Report amount mismatch in money
- [ ] Hosted deployment decision

## Documentation

- [Architecture Overview](docs/architecture/overview.md)
- [ADR-001: Initial Architecture Choice](docs/ADR/ADR-0001-initial-architecture-choice.md)
- [Domain Glossary](docs/domain/glossary.md)

## License

MIT
