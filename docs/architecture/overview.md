# Architecture Overview

**Last Updated:** 22 March 2026  
**Status:** Draft

---

## System Purpose

RevRecon: Detect where usage and billing don't match and explain why.

---

## Architectural Strategy

### Decision: Layered → Refactor

**Approach:** Start with simple Layered Architecture, refactor when pain is felt.

```
Phase 1: Layered (Controller → Service → Repository)
    ↓
Phase 2: Feel the pain? → ADR + refactor to Clean/Hexagonal
    ↓
Phase 3: Event Sourcing experiment (separate bounded context)
```

**Rationale:** 
- You don't understand WHY Hexagonal until you feel pain from Layered
- When adding second data source or replacing DB — that's the moment
- Document the pain and decision in ADR

**Reference:** ADR-001 (to be written)

---

## Architecture Evolution Plan

| Phase | Style | Focus |
|-------|-------|-------|
| Phase 1 | Layered | Simple start, learn basics |
| Phase 2 | Evaluate | Do we feel pain? What specifically? |
| Phase 2-3 | Clean/Hexagonal | If pain warrants refactor |
| Phase 2-3 | Event Sourcing | Experiment for audit trail |
| Phase 3 | CQRS | Optional, for read optimization |

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

---

## Architectural Styles to Explore

| Style | Description | When to try |
|-------|-------------|-------------|
| Layered | Controller → Service → Repository | Phase 1 (start) |
| Clean Architecture | Use Cases, Entities, Interfaces | Phase 2 (if pain) |
| Hexagonal | Ports & Adapters | Phase 2 (if pain) |
| Event-Driven | Async communication via events | Phase 2 |
| Event Sourcing | Store events, derive state | Phase 2-3 |
| CQRS | Separate read/write models | Phase 3 |

---

## Core Components (Planned)

| Component | Responsibility | Phase |
|-----------|---------------|-------|
| UsageController | POST /events, GET /usage | 1 |
| BillingController | POST /billing, GET /billing | 1 |
| DiscrepancyController | GET /discrepancies | 2 |
| ReconciliationService | Compare usage vs billing | 2 |
| DiscrepancyDetector | Find and classify issues | 2 |
| ExplainabilityEngine | Analyze root cause | 3 |

---

## Data Flow

```
Usage Event 
    ↓ POST /events
UsageEventRepository
    ↓
    ├──────────────────────┐
    ↓                      ↓
BillingRecord      ReconciliationService
    ↓                      ↓
    └──────────────────────┤
                           ↓
                    DiscrepancyDetector
                           ↓
                      Discrepancy
                           ↓
                  ExplainabilityEngine
                           ↓
                      Explanation
```

---

## Production Concerns (Built In)

| Concern | Pattern | Phase |
|---------|---------|-------|
| Idempotency | Idempotency key per event | 1 |
| Audit trail | Created/updated timestamps, user_id | 1 |
| Versioning | API: /v1/, Schema: Flyway | 1 |
| Concurrency | Optimistic locking (@Version) | 2 |
| Error handling | Retry + dead letter table | 2 |

---

## Key Design Decisions

All documented in `/docs/adr/`

| ADR | Decision | Date | Status |
|-----|----------|------|--------|
| ADR-001 | Start with Layered, refactor later | TBD | 🔴 TODO |
| ADR-002 | Event Sourcing for audit (Phase 2) | TBD | 🔴 TODO |
| ADR-003 | CQRS decision (Phase 3) | TBD | 🔴 TODO |

---

## Tech Stack

| Layer | Technology | Why |
|-------|-----------|-----|
| Language | Java 17+ | Work alignment, learning |
| Framework | Spring Boot 3.x | Industry standard |
| Database | PostgreSQL | Reliable, good for learning |
| Migrations | Flyway | Versioned schema |
| Testing | JUnit 5, TestContainers | Integration tests |
| Data Processing | Python (later) | Industry standard for data |

---

## Diagrams

Location: `/docs/architecture/diagrams/`

| Diagram | Purpose | Status |
|---------|---------|--------|
| System Context (C4) | Big picture | 🔴 TODO |
| Container (C4) | Main components | 🔴 TODO |
| Data Model | Entities & relationships | 🔴 TODO |
| Sequence | Key flows | 🔴 TODO |

---

## Evolution Log

| Date | Change | ADR |
|------|--------|-----|
| 22 Mar 2026 | Initial structure | — |
| 22 Mar 2026 | Decision: Layered → refactor strategy | ADR-001 (pending) |
