# RevRecon

Detect where usage and billing don't match and explain why.

## What is this

RevRecon is a backend project exploring how billing systems fail in practice.

It focuses on a simple question:

> Why does usage not match what gets billed?

## What it does

- Ingests usage events and billing records
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
│   ├── adr/           # Architecture Decision Records
│   ├── architecture/  # System design, patterns
│   └── domain/        # Domain glossary
└── .context/          # Project context files
```

## Status

**Phase 1: Core Model + Ingestion** — In Progress

- [x] Project setup
- [x] ADR-001: Layered Architecture
- [ ] Data model (Customer, Plan, UsageEvent, BillingRecord)
- [ ] Ingestion endpoints
- [ ] Reconciliation logic

## Documentation

- [Architecture Overview](docs/architecture/overview.md)
- [ADR-001: Initial Architecture Choice](docs/adr/0001-initial-architecture-choice.md)
- [Domain Glossary](docs/domain/glossary.md)

## License

MIT
