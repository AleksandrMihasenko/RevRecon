# RevRecon Development Guide

## Goal

Detect where product usage and billing don't match and explain why — in money.

**Current wedge: Billing Correctness for usage/hybrid pricing.**

> Connect usage events + billing records → see where they diverge: who's underbilled, who's overbilled, per client, in money.

---

## Scope

Focus on:
- usage → billing reconciliation
- pricing rules (expected charges)
- discrepancy detection and reporting
- explainability of mismatches

Out of scope:
- full billing system
- payment processing
- complex UI (simple dashboard OK later)

---

## Working approach

Work in small, incremental steps.

For each change:
1. Start from a concrete scenario (realistic failure case)
2. Define expected behavior
3. Implement minimal solution
4. Validate with tests
5. Refine if needed

Avoid building generic abstractions too early.

---

## Tech stack

- Java 21 (Spring Boot 4.0.4) — core logic
- PostgreSQL 16+ — storage
- Flyway — migrations
- Raw JDBC (NamedParameterJdbcTemplate) — data access
- JUnit 5, TestContainers — testing

---

## Core scenarios

The system should support:

1. **Missing usage** — Usage exists but is not billed
2. **Duplicated usage** — Same usage counted multiple times
3. **Wrong pricing** — Usage billed with incorrect pricing rules
4. **Timing mismatch** — Usage assigned to the wrong billing period

---

## Design principles

- Prefer explicit domain models over generic abstractions
- Optimize for correctness, not performance (early stage)
- Keep logic easy to trace and debug
- Make discrepancies explainable, not just detectable
- Separate INSERT/UPDATE methods (billing = money = clarity)
- Repository converts technical → domain exceptions
- Technologies are introduced only when justified by real needs

---

## Key architectural decisions

- Layered architecture (Controller → Service → Repository)
- Raw JDBC over Spring Data JDBC (learn trade-offs)
- Separate Request/Response DTOs
- RETURNING * with queryForObject() for inserts
- kebab-case URLs
- RevReconException hierarchy for domain errors
- 409 Conflict for duplicate idempotency keys

---

## Testing

- Scenario-based tests
- Each discrepancy type: a minimal failing case + a passing case after implementation
- Integration tests with TestContainers

---

## Code guidelines

- Keep functions small and focused
- Name things based on domain (usage, billing, invoice, discrepancy, pricing rule)
- Avoid premature optimization and over-engineering
- snake_case in SQL, camelCase in Java

---

## Iteration strategy

```
V1 — Billing Correctness wedge:
  ├── Ingestion: POST /usage-events, POST /billing-records  ✅ done
  ├── Pricing rules config: 1-2 simple models (per-event, tiered)
  ├── Discrepancy engine: expected (usage × rules) vs billed
  ├── Discrepancy report: under/over/match, per client, in money
  └── Deploy (Docker) + demo dataset

DONE = deployed + 2 pricing models + demo report

After V1: expand one capability at a time (more pricing models,
alerts, analytics), each justified by a real need.
```

---

## Agent behavior

- Explain approach before writing code
- Do not generate full implementations by default
- Show structure and direction, not ready-to-copy code
- When starting a new feature: think through the data flow before coding
