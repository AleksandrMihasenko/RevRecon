# Architecture Overview

**Last Updated:** 3 May 2026  
**Status:** Phase 1 — Ingestion and First Read Path Complete

---

## System Purpose

RevRecon: Detect where usage and billing don't match and explain why.

---

## Architectural Strategy

### Decision: Layered → Refactor

**Approach:** Start with simple Layered Architecture, refactor when pain is felt.

```
Phase 1: Layered (Controller → Service → Repository)  ← CURRENT
    ↓
Phase 2+: Feel the pain? → ADR + refactor to Clean/Hexagonal
    ↓
Phase 4: One advanced experiment (Event Sourcing / CQRS / Alerts / Simulation)
```

**Rationale:** 
- You don't understand WHY Hexagonal until you feel pain from Layered
- When adding second data source or replacing DB — that's the moment
- Document the pain and decision in ADR

**Reference:** [ADR-001: Initial Architecture Choice](../adr/0001-initial-architecture-choice.md) ✅

---

## Architecture Evolution Plan

| Phase | Style | Focus | Status |
|-------|-------|-------|--------|
| Phase 1 | Layered | Simple start, learn basics | 🟡 In Progress |
| Phase 2 | Evaluate | Do we feel pain? What specifically? | 🔴 TODO |
| Phase 4 | TBD | One experiment: Event Sourcing / CQRS / Alerts / Simulation | 🔴 TODO |

---

## Current Architecture (Phase 1)

```
┌─────────────────────────────────────────────────────────┐
│                     API Layer                            │
│         (Controllers, DTOs, Validation)                  │
├─────────────────────────────────────────────────────────┤
│                   Service Layer                          │
│    (Business logic, orchestration, domain rules)         │
├─────────────────────────────────────────────────────────┤
│                 Repository Layer                         │
│        (Data access, Spring Data JDBC)                   │
├─────────────────────────────────────────────────────────┤
│                    Database                              │
│                  (PostgreSQL)                            │
└─────────────────────────────────────────────────────────┘
```

**Package Structure:**
```
com.revrecon.backend/
├── controller/
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
| Layered | Controller → Service → Repository | Phase 1 | 🟡 Current |
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
| DiscrepancyController | GET /discrepancies | 2 | 🔴 TODO |
| ReconciliationService | Compare usage vs billing | 2 | 🔴 TODO |
| DiscrepancyDetector | Find and classify issues | 2 | 🔴 TODO |
| ExplainabilityEngine | Analyze root cause | 3 | 🔴 TODO |

---

## Data Flow

```
GET /api/usage-billing-summary
    ↓
UsageBillingSummaryController
    ↓
UsageBillingSummaryService
    ├── UsageEventRepository.getUsageTotalsByMetric(...)
    └── BillingRecordRepository.getBilledTotal(...)
    ↓
UsageBillingSummaryResponse
```

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

All documented in `/docs/adr/`

| ADR | Decision | Date | Status |
|-----|----------|------|--------|
| [ADR-001](../adr/0001-initial-architecture-choice.md) | Start with Layered, refactor later | 22 Mar 2026 | ✅ Accepted |
| ADR-002 | Phase 4 experiment choice | TBD | 🔴 TODO |

---

## Tech Stack

| Layer | Technology | Version | Status |
|-------|-----------|---------|--------|
| Language | Java | 21 (LTS) | ✅ |
| Framework | Spring Boot | 4.0.4 | ✅ |
| Database | PostgreSQL | 16+ | ✅ |
| Migrations | Flyway | — | ✅ |
| Testing | JUnit 5, TestContainers | — | 🔴 TODO |
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
