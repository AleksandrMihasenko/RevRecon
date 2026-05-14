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
| Testing | JUnit 5, Mockito; TestContainers planned | — | Unit/controller tests now, integration tests later |
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
- [x] DTOs (UsageEventRequest, UsageEventResponse)
- [x] Usage events ingestion (POST /api/usage-events)
- [x] Idempotency handling (409 Conflict on duplicate)
- [x] Audit fields (created_at, updated_at)
- [x] Request DTO validation for usage ingestion
- [x] Structured error responses for usage ingestion
- [x] Global exception handling for 400/409 on usage ingestion
- [x] Controller tests for POST /api/usage-events (201, 400, 409)
- [x] Billing records ingestion (POST /api/billing)
- [x] Billing DTO validation and domain error path for invalid billing periods
- [x] Controller tests for POST /api/billing (201, 400, 409, invalid period)
- [x] BillingRecordService unit tests for happy path, duplicate, invalid period, and equal boundaries
- [x] Basic aggregation queries
- [x] Read-side summary endpoint (`GET /api/usage-billing-summary`)
- [x] Controller test for `GET /api/usage-billing-summary`
- [x] UsageBillingSummaryService unit test
- [x] Local Docker Compose runtime baseline
- [x] Lightweight health endpoint (`GET /api/health`)
- [x] Controller test for `GET /api/health`
- [ ] TestContainers integration tests for ingestion and summary SQL queries (moved to deploy prep / Phase 2 follow-up)

**Milestone:** Can ingest usage and billing data, read back usage-by-metric plus billed totals for a period, and verify the backend is alive through a lightweight health endpoint.

**Learning focus:** Domain modeling, REST, persistence, SQL basics, idempotency.

**Interview questions after:**
- "Design usage-based billing data model" ✅
- "How to prevent duplicate billing events?" ✅
- "DTO vs Entity — why separate?" ✅
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
| Phase 1 | P0 MUST | April 2026 | ✅ Closed |
| Phase 2 | P0 MUST | May 2026 | 🔴 TODO |
| Phase 3 | P1 NICE | June 2026 | 🔴 TODO |
| Phase 4 | P1 NICE | June 2026 | 🔴 TODO |
| Phase 5 | P2 NICE | Late June 2026 | 🔴 TODO |

**Hard constraint:** Phase 1-2 must be complete for June market entry.

---

## Production Concerns (Built In)

| Concern | Why | Phase | Status |
|---------|-----|-------|--------|
| Idempotency keys | Billing = money, no duplicates | 1 | ✅ Done |
| Audit fields | Who changed what, when | 1 | ✅ In schema |
| Exception hierarchy | Domain errors vs technical | 1 | ✅ Done |
| DTO layer | API/DB separation | 1 | ✅ Done |
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
| Separate Request/Response DTOs | Different fields, versioning flexibility | 5 Apr 2026 |
| RETURNING * for insert | One query, get full entity back | 5 Apr 2026 |
| Positioning: Billing & Revenue Systems | Market-validated niche, entry point | 19 Apr 2026 |
| Billing idempotency with 409 on duplicate | Simpler retry-protection model for Phase 1 learning | 26 Apr 2026 |

Full decision log: [PRIVATE_DECISIONS.md](./.context/PRIVATE_DECISIONS.md)

---

## Current Focus

**Phase 1:** Closed on 14 May 2026. Test coverage exists at controller/service level for ingestion, summary, and health-check flows.

**Next session start:** Begin Phase 2 reconciliation/discrepancy detection from one concrete scenario, preferably missing usage. Start with data flow before code.

Planned Phase 2 direction:
- Draw the first reconciliation data flow: usage events + billing records + plan prices → expected amount → billed amount → discrepancy.
- Pick one scenario first instead of designing every discrepancy type at once.
- Define expected behavior and tests before implementation.
- Keep deploy improvements as follow-up work unless they directly support Phase 2.

TestContainers integration tests remain a follow-up item and are intentionally postponed until deploy preparation and reconciliation work begin.

**Phase 1 closure checklist:**
- [x] Review current Phase 1 data flows for ingestion and summary endpoints
- [x] Validate architecture understanding against current implementation
- [x] Add compact architecture/data-flow documentation for current endpoints
- [x] Create backend Dockerfile
- [x] Create local Docker Compose setup (backend + PostgreSQL)
- [x] Externalize configuration via environment variables
- [x] Validate containerized local startup flow
- [x] Add lightweight health endpoint
- [x] Add controller test for health endpoint
- [x] Close Phase 1 and move remaining improvements to follow-up

Current Docker milestone:
- Backend Dockerfile uses a multi-stage build: Maven/JDK build stage and JRE runtime stage
- Docker Compose starts backend + PostgreSQL locally
- PostgreSQL data is stored in a named Docker volume
- Local env values are documented through `deploy/env/.env.example`
- Local cleanup is possible with `docker-compose down -v`
- Backend exposes `GET /api/health` for a lightweight liveness check

Follow-up improvements, not Phase 1 blockers:
- Run the full backend test suite after the final docs update
- Verify `GET /api/health` through Docker Compose
- Decide future hosted deployment direction (Railway, Render, VPS, etc.)
- Add TestContainers integration tests for SQL-backed ingestion and summary flows
- Consider Spring Actuator or DB readiness checks later, when hosted deployment needs them

---

## Deployment Learning Goals

| Goal | Why |
|------|-----|
| Docker basics | Learn reproducible backend environments |
| Docker Compose | Run backend + PostgreSQL together |
| Environment variables | Separate config from code |
| Container networking | Understand service communication |
| Hosted deployment | Learn how backend systems are exposed publicly |
| Logs and runtime debugging | Observe application behavior outside IDE |

Current deployment strategy:
1. Local Docker Compose first
2. Hosted deployment second (Railway/Render likely first choice)
3. VPS/Linux exploration later if needed

Reasoning:
The goal is not DevOps specialization yet. The current focus is learning how backend systems are packaged, run, debugged, and exposed in realistic environments while keeping the project simple enough to continue Phase 2 reconciliation work.

**Last Updated:** 14 May 2026
**Status:** Phase 1 closed — ingestion endpoints, idempotency handling, validation/error paths, summary read path, local Docker baseline, health endpoint, and controller/service tests are in place. Integration tests and hosted deployment remain planned for Phase 2 / deploy follow-up.
