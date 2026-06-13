# Architecture Overview

**Last Updated:** 13 June 2026
**Status:** Phase 2 — First reconciliation rule exposed through API

---

## System Purpose

RevRecon: Detect where usage and billing don't match and explain why.

---

## Architectural Strategy

### Decision: Layered → Refactor

**Approach:** Start with simple Layered Architecture, refactor when pain is felt.

```
Phase 1: Layered (Controller → Service → Repository)  ← CLOSED
    ↓
Phase 2: Add reconciliation rules in the Service layer; feel the pain before refactoring
    ↓
Phase 4: One advanced experiment (Event Sourcing / CQRS / Alerts / Simulation)
```

**Rationale:** 
- You don't understand WHY Hexagonal until you feel pain from Layered
- When adding second data source or replacing DB — that's the moment
- Document the pain and decision in ADR

**Reference:** [ADR-001: Initial Architecture Choice](../ADR/ADR-0001-initial-architecture-choice.md) ✅

---

## Architecture Evolution Plan

| Phase | Style | Focus | Status |
|-------|-------|-------|--------|
| Phase 1 | Layered | Simple start, learn basics | ✅ Closed |
| Phase 2 | Layered + evaluation | Reconciliation rules, discrepancy detection, watch for service complexity | 🟡 Started |
| Phase 4 | TBD | One experiment: Event Sourcing / CQRS / Alerts / Simulation | 🔴 TODO |

---

## Current Architecture (Phase 2)

```
┌─────────────────────────────────────────────────────────┐
│                     API Layer                            │
│         (Controllers, DTOs, Validation)                  │
├─────────────────────────────────────────────────────────┤
│                   Service Layer                          │
│    (Business logic, orchestration, domain rules)         │
├─────────────────────────────────────────────────────────┤
│                 Repository Layer                         │
│        (Data access, raw JDBC + SQL)                     │
├─────────────────────────────────────────────────────────┤
│                    Database                              │
│                  (PostgreSQL)                            │
└─────────────────────────────────────────────────────────┘
```

**Package Structure:**
```
com.revrecon.backend/
├── controller/
│   ├── HealthController
│   ├── UsageEventController
│   ├── BillingController
│   └── UsageBillingSummaryController
├── dto/
├── service/
├── repository/
└── model/
```

---

## Data Model

```
┌──────────────┐       ┌──────────────┐
│   Customer   │       │     Plan     │
├──────────────┤       ├──────────────┤
│ id           │       │ id           │
│ name         │       │ name         │
│ created_at   │       │ prices (JSON)│
│ updated_at   │       │ created_at   │
└──────┬───────┘       │ updated_at   │
       │               └──────┬───────┘
       │                      │
       │    ┌─────────────────┘
       │    │
       ▼    ▼
┌──────────────────┐
│   Subscription   │
├──────────────────┤
│ id               │
│ customer_id (FK) │
│ plan_id (FK)     │
│ discount         │
│ start_date       │
│ end_date         │
│ status (ENUM)    │
│ created_at       │
│ updated_at       │
└──────────────────┘

┌──────────────────┐     ┌──────────────────┐
│   UsageEvent     │     │  BillingRecord   │
├──────────────────┤     ├──────────────────┤
│ id               │     │ id               │
│ idempotency_key  │     │ customer_id (FK) │
│ customer_id (FK) │     │ period_start     │
│ metric           │     │ period_end       │
│ quantity         │     │ amount           │
│ timestamp        │     │ status (ENUM)    │
│ created_at       │     │ created_at       │
│ updated_at       │     │ updated_at       │
└──────────────────┘     └──────────────────┘

Derived domain model:

```
┌──────────────────┐
│   Discrepancy    │
├──────────────────┤
│ customerId       │
│ type             │
│ periodStart      │
│ periodEnd        │
│ explanation      │
└──────────────────┘
```

`Discrepancy` is not persisted yet. It is derived dynamically from usage and billing source data.
```

**Key design decisions:**
- `prices` as JSONB in Plan (flexible, no separate PricingRule table)
- `idempotency_key` with UNIQUE constraint (prevents duplicate events)
- ENUMs for status fields (DB enforces valid values)
- All tables have audit fields (`created_at`, `updated_at`)

**Details:** See [Domain Glossary](../domain/glossary.md)

---

## Architectural Styles to Explore

| Style | Description | When to try | Status |
|-------|-------------|-------------|--------|
| Layered | Controller → Service → Repository | Phase 1 | ✅ Current |
| Clean Architecture | Use Cases, Entities, Interfaces | If pain felt | 🔴 TODO |
| Hexagonal | Ports & Adapters | If pain felt | 🔴 TODO |
| Event-Driven | Async communication via events | Phase 2+ | 🔴 TODO |
| Event Sourcing | Store events, derive state | Phase 4 option | 🔴 TODO |
| CQRS | Separate read/write models | Phase 4 option | 🔴 TODO |

---

## Core Components (Planned)

| Component | Responsibility | Phase | Status |
|-----------|---------------|-------|--------|
| UsageEventController | POST /usage-events | 1 | ✅ Done |
| BillingController | POST /billing | 1 | ✅ Done |
| UsageBillingSummaryController | GET /usage-billing-summary | 1 | ✅ Done |
| UsageBillingSummaryService | Aggregate usage + billed totals for period | 1 | ✅ Done |
| HealthController | GET /health lightweight liveness check | 1 / Deploy prep | ✅ Done |
| DiscrepancyService | Detect first discrepancy from source data | 2 | ✅ First rule done |
| DiscrepancyController | GET /discrepancies | 2 | ✅ Done |
| ReconciliationService | Compare usage vs billing | 2 | 🔴 TODO |
| DiscrepancyDetector | Find and classify issues | 2 | 🔴 TODO |
| ExplainabilityEngine | Analyze root cause | 3 | 🔴 TODO |

---

## Data Flow

### DiscrepancyService: UNBILLED_USAGE

Purpose: detect the first revenue leakage scenario from already ingested usage and billing data.

Rule:

```text
If a customer has usage totals in a period
and no billing record exists for the exact same customer + period,
return UNBILLED_USAGE.
```

Flow:
1. Client calls `GET /api/discrepancies` with `customerId`, `periodStart`, and `periodEnd`.
2. `DiscrepancyController` delegates to `DiscrepancyService`.
3. Service rejects an invalid period before calling repositories.
4. Service calls `UsageEventRepository.getUsageTotalsByMetric(...)`.
5. Service calls `BillingRecordRepository.findByCustomerIdAndPeriod(...)`.
6. If usage totals are present and billing record is missing, service returns `UNBILLED_USAGE`.
7. Controller returns `200 OK` with a collection of `DiscrepancyResponse` values; no matches return an empty collection.

Design notes:
- This is service-level business logic, not repository logic.
- The rule is intentionally narrow: `UNBILLED_USAGE` means billing record missing, not amount mismatch.
- Billing exists but no usage supports it is a different future discrepancy type.
- No `discrepancies` table exists yet; persistence can be added when reconciliation history or workflow is needed.

### GET /api/health

Purpose: lightweight liveness check for local runtime and future deployment checks.

Flow:
1. Client sends `GET /api/health`.
2. `HealthController` returns `HealthResponse`.
3. Controller returns `200 OK` with `{"status":"UP"}`.

Design notes:
- This is a liveness check, not a readiness check.
- It does not check PostgreSQL yet.
- DB readiness or Spring Actuator can be added later if hosted deployment needs stronger operational checks.

### GET /api/usage-billing-summary

Purpose: return usage totals and billed total for a customer and exact billing period.

Flow:
1. Client sends `customerId`, `periodStart`, and `periodEnd` as query parameters.
2. `UsageBillingSummaryController` receives the request.
3. `UsageBillingSummaryService` validates the period.
4. Service calls `UsageEventRepository` to aggregate usage by metric for the period.
5. Service calls `BillingRecordRepository` to calculate billed total for the same exact period.
6. Usage totals are mapped to `UsageByMetricResponse`.
7. Service returns `UsageBillingSummaryResponse`.
8. Controller returns `200 OK`.

Failure cases:
- Invalid period → `InvalidBillingPeriodException` → `400 Bad Request`

Design notes:
- Usage is grouped by metric because different metrics may use different units.
- Billed total uses `COALESCE(SUM(amount), 0)` to avoid leaking SQL `NULL` into service logic.
- Phase 1 uses exact period matching for billing records to keep the read path simple and predictable.
- This endpoint is a read-side summary, not reconciliation yet.

### POST /api/usage-events

Purpose: ingest usage events from external systems.

Flow:
1. Client sends `UsageEventRequest` to `POST /api/usage-events`.
2. `UsageEventController` validates the request body.
3. `UsageEventService` maps the request DTO to `UsageEvent`.
4. `UsageEventRepository` inserts the event using raw SQL via `NamedParameterJdbcTemplate`.
5. PostgreSQL enforces uniqueness of `idempotency_key`.
6. On success, `INSERT ... RETURNING *` returns the inserted row.
7. Repository maps the row back to `UsageEvent`.
8. Service maps it to `UsageEventResponse`.
9. Controller returns `201 Created`.

Failure cases:
- Invalid request body → `400 Bad Request`
- Duplicate `idempotency_key` → Repository converts `DuplicateKeyException` to `DuplicateEventException` → `GlobalExceptionHandler` returns `409 Conflict`

Design notes:
- Request/response DTOs keep the API contract separate from database entities.
- Repository hides infrastructure exceptions and exposes domain exceptions.
- Database-level uniqueness is used for idempotency protection.
---

## Production Concerns (Built In)

| Concern | Pattern | Phase | Status |
|---------|---------|-------|--------|
| Idempotency | UNIQUE constraint on idempotency_key | 1 | ✅ In schema |
| Audit trail | created_at / updated_at on all tables | 1 | ✅ In schema |
| Versioning | Schema: Flyway | 1 | ✅ Configured |
| Concurrency | Optimistic locking (@Version) | 2 | 🔴 TODO |
| Error handling | Retry + dead letter table | 2 | 🔴 TODO |

---

## Key Design Decisions

All documented in `/docs/ADR/`

| ADR | Decision | Date | Status |
|-----|----------|------|--------|
| [ADR-001](../ADR/ADR-0001-initial-architecture-choice.md) | Start with Layered, refactor later | 22 Mar 2026 | ✅ Accepted |
| [ADR-002](../ADR/ADR-0002-retry-protection-and-duplicate-prevention-choice.md) | Retry protection and duplicate prevention | 26 Apr 2026 | ✅ Accepted |

---

## Tech Stack

| Layer | Technology | Version | Status |
|-------|-----------|---------|--------|
| Language | Java | 21 (LTS) | ✅ |
| Framework | Spring Boot | 4.0.4 | ✅ |
| Database | PostgreSQL | 16+ | ✅ |
| Migrations | Flyway | — | ✅ |
| Testing | JUnit 5, Mockito; TestContainers planned | — | 🟡 Controller/service tests done |
| API Docs | Swagger UI | — | 🔴 TODO |

---

## Diagrams

Location: `/docs/architecture/diagrams/`

| Diagram | Purpose | Status |
|---------|---------|--------|
| System Context (C4) | Big picture | 🔴 TODO |
| Container (C4) | Main components | 🔴 TODO |
| Data Model | Entities & relationships | ✅ Above |
| Sequence | Key flows | 🔴 TODO |

---

## Evolution Log

| Date | Change | ADR |
|------|--------|-----|
| 22 Mar 2026 | Initial structure | — |
| 22 Mar 2026 | Layered Architecture decision | ADR-001 ✅ |
| 22 Mar 2026 | Spring Boot 4.0.4 + Java 21 setup | — |
| 22 Mar 2026 | Package structure created | — |
| 28 Mar 2026 | Data model (V1 migration) | — |
| 3 May 2026 | First read-side summary endpoint and aggregation queries | — |
| 7 May 2026 | Summary controller/service tests added; Phase 1 functional scope complete | — |
| 14 May 2026 | Health endpoint added; Phase 1 closed | — |
| 17 May 2026 | First reconciliation rule implemented: `UNBILLED_USAGE` | — |

---

## Follow-Up Improvements

Current Phase 2 follow-ups:
- Add Testcontainers coverage for the SQL-backed reconciliation path.
- Select the next discrepancy type from a concrete failure scenario.
- Add TestContainers integration tests for SQL-backed ingestion and summary flows.
- Decide hosted deployment direction.
- Consider Spring Actuator or DB readiness checks when deployment requirements become clearer.
