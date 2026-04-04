# Project: RevRecon

## Description

Revenue / Billing Discrepancy & Explainability Engine.
Detect where usage and billing don't match and explain why.

**Primary goal:** Learning platform for billing domain, data quality, and backend skills.
**Secondary goal:** Portfolio project for interviews.

**Type:** Backend-heavy. Frontend/CLI optional later.

---

## Goals

1. Backend transition (Java/Spring practice)
2. Billing/Data domain expertise
3. Architecture & patterns exploration
4. Production-ready portfolio piece
5. Interview preparation

---

## Tech Stack

| Layer | Technology | Version | Why |
|-------|-----------|---------|-----|
| Language | Java | 21 (LTS) | Work alignment, backend transition |
| Framework | Spring Boot | 4.0.4 | Industry standard |
| Database | PostgreSQL | 16+ | Reliable, good for learning |
| Migrations | Flyway | — | Versioned schema |
| Testing | JUnit 5, TestContainers | — | Integration tests |
| API docs | Swagger UI | — | Demo without frontend |
| Data access | Raw JDBC | — | Learning trade-offs vs Spring Data |

### Future (Optional)

| Layer | Technology | When |
|-------|-----------|------|
| Frontend | React or Vue + Tailwind | Phase 5+ if time |
| CLI | Picocli or Spring Shell | Phase 5+ if time |

---

## Core Scenarios

| Type | Description |
|------|-------------|
| Missing usage | Usage exists but is not billed |
| Duplicated usage | Same usage counted multiple times |
| Wrong pricing | Usage billed with incorrect rate |
| Timing mismatch | Usage assigned to wrong billing period |

---

## Roadmap

### Phase 1: Core Model + Ingestion (April 2026) — P0 MUST HAVE

**Deliverables:**
- [x] Spring Boot, PostgreSQL, Flyway setup
- [x] Simple layered architecture (ADR-001)
- [x] Core entities: Customer, Plan, Subscription, UsageEvent, BillingRecord
- [x] Java entity classes with enums
- [x] Repositories (raw JDBC) — 5/5 done
- [x] Exception hierarchy (RevReconException, DuplicateEventException)
- [ ] Usage events ingestion (POST /events)
- [ ] Billing records ingestion (POST /billing)
- [ ] Idempotency handling (409 Conflict on duplicate)
- [x] Audit fields (created_at, updated_at)
- [ ] Basic aggregation queries
- [ ] Integration tests for ingestion endpoints

**Milestone:** Can ingest usage and billing data and calculate expected vs billed totals.

**Learning focus:** Domain modeling, REST, persistence, SQL basics, idempotency.

**Interview questions after:**
- "Design usage-based billing data model" ✅
- "How to prevent duplicate billing events?" ✅
- "SQL: calculate total usage per customer"

---

### Phase 2: Reconciliation + Discrepancy Detection (May 2026) — P0 MUST HAVE

**Deliverables:**
- [ ] Optimistic locking (version column)
- [ ] Comparison logic: expected vs billed
- [ ] Discrepancy types: missing, duplicate, wrong price, timing mismatch
- [ ] GET /discrepancies endpoint
- [ ] Revenue metrics: expected revenue, billed revenue, discrepancy amount
- [ ] Scheduled reconciliation job
- [ ] Error handling (what if reconciliation fails mid-way?)
- [ ] Scenario tests for each discrepancy type

**Milestone:** System detects and classifies billing mismatches.

**Learning focus:** Reconciliation logic, SQL, scheduled jobs, scenario testing.

**Interview questions after:**
- "How to detect revenue leakage?"
- "Design a reconciliation system"
- "How to handle failed batch jobs?"

---

### Phase 3: Explainability (June 2026) — P1 NICE TO HAVE

**Deliverables:**
- [ ] Explanation layer for discrepancies
- [ ] Root cause categories: missing event, duplicate event, pricing mismatch, timing mismatch, config/manual issue
- [ ] GET /discrepancies/{id}/explain
- [ ] Historical analysis of discrepancy patterns

**Milestone:** System explains WHY a discrepancy happened.

**Learning focus:** Traceability, domain reasoning, explainability.

**Interview questions after:**
- "How would you explain billing errors to users?"
- "Design an audit/traceability system"

---

### Phase 4: One Advanced Experiment (June 2026) — P1 NICE TO HAVE

**Choose ONE when ready:**

| Option | Complexity | Interview value |
|--------|------------|-----------------|
| Event Sourcing | High | High — audit, replay |
| CQRS | Medium | High — read optimization |
| Alerts/thresholds | Low | Medium — ops, monitoring |
| Simulation | Medium | Medium — business value |

**Decision:** TBD when Phase 3 complete.

**Milestone:** One advanced system design concept applied with clear reasoning.

**Learning focus:** Architecture trade-offs, ADR documentation.

---

### Phase 5: Demo Readiness (Late June 2026) — P2 NICE TO HAVE

**Deliverables:**
- [ ] Docker
- [ ] Small demo dataset
- [ ] End-to-end demo flow
- [ ] README polish for recruiters

**Optional extensions:**
- [ ] Simple dashboard (React/Vue, 1-2 screens: discrepancy list + details)
- [ ] CLI tool (`revrecon reconcile`, `revrecon explain`)

**Milestone:** Project is easy to run and explain in interviews.

---

## Timeline Summary

| Phase | Priority | Target | Status |
|-------|----------|--------|--------|
| Phase 1 | P0 MUST | April 2026 | 🟡 In Progress |
| Phase 2 | P0 MUST | May 2026 | 🔴 TODO |
| Phase 3 | P1 NICE | June 2026 | 🔴 TODO |
| Phase 4 | P1 NICE | June 2026 | 🔴 TODO |
| Phase 5 | P2 NICE | Late June 2026 | 🔴 TODO |

**Hard constraint:** Phase 1-2 must be complete for June market entry.

---

## Production Concerns (Built In)

| Concern | Why | Phase | Status |
|---------|-----|-------|--------|
| Idempotency keys | Billing = money, no duplicates | 1 | ✅ In schema + repository |
| Audit fields | Who changed what, when | 1 | ✅ In schema |
| Exception hierarchy | Domain errors vs technical | 1 | ✅ Done |
| Optimistic locking | Prevent lost updates | 2 | 🔴 TODO |
| Error handling | Graceful failure, retry | 2 | 🔴 TODO |

---

## Key Decisions Made

| Decision | Why | Date |
|----------|-----|------|
| Layered Architecture | Simple start, refactor when pain | 22 Mar 2026 |
| Raw JDBC over Spring Data | Feel the pain, understand trade-offs | 29 Mar 2026 |
| Separate INSERT/UPDATE methods | Clarity > convenience in billing | 29 Mar 2026 |
| 409 Conflict for duplicates | Client must know about duplicate | 29 Mar 2026 |
| JSONB prices as String | Parse in service, simple for Phase 1 | 29 Mar 2026 |
| Repository converts exceptions | Hides infrastructure, domain language | 4 Apr 2026 |

---

## Current Focus

**Phase 1:** POST /events endpoint → Service layer → 409 Conflict handling → Tests

---

**Last Updated:** 4 April 2026
**Status:** Phase 1 in progress — repositories done, starting endpoints
