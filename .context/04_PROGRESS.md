# Project Progress

**Project:** RevRecon
**Started:** 21 March 2026
**Current Phase:** Phase 1 — Core Model + Ingestion
**Market entry:** June 2026

---

## Priorities

| Phase | Priority | Status |
|-------|----------|--------|
| Phase 1-2 | P0 MUST HAVE | 🟡 In Progress |
| Phase 3-5 | P1/P2 NICE TO HAVE | 🔴 TODO |

---

## Current Status

| Area | Status |
|------|--------|
| Repository | ✅ |
| README, LICENSE | ✅ |
| AGENTS.md, CLAUDE.md | ✅ |
| .context/ structure | ✅ |
| docs/ structure | ✅ |
| Plan validated | ✅ |
| ADR-001 (Layered Architecture) | ✅ |
| Spring Boot project setup | ✅ |
| PostgreSQL + Flyway config | ✅ |
| Package structure | ✅ |
| **Data model (V1 migration)** | ✅ |
| First entity (Java) | 🔴 TODO |
| First endpoint | 🔴 TODO |

---

## Phase 1: Core Model + Ingestion

**Target:** April 2026
**Priority:** P0 MUST HAVE

### Setup ✅ DONE
| Task | Status |
|------|--------|
| Spring Boot 4.0.4, Java 21 | ✅ |
| PostgreSQL config | ✅ |
| Flyway config | ✅ |
| ADR-001: Layered architecture | ✅ |
| Package structure (controller, service, repository, model) | ✅ |

### Data Model ✅ DONE
| Task | Status |
|------|--------|
| V1__init.sql migration | ✅ |
| customers table | ✅ |
| plans table (JSONB prices) | ✅ |
| subscriptions table (ENUM status) | ✅ |
| usage_events table (idempotency_key UNIQUE) | ✅ |
| billing_records table (ENUM status) | ✅ |

### Entities (Java)
| Task | Status |
|------|--------|
| Customer entity | 🔴 TODO |
| Plan entity | 🔴 TODO |
| Subscription entity | 🔴 TODO |
| UsageEvent entity | 🔴 TODO |
| BillingRecord entity | 🔴 TODO |

### API
| Task | Status |
|------|--------|
| POST /events (usage ingestion) | 🔴 TODO |
| POST /billing (billing records) | 🔴 TODO |
| Idempotency handling | 🔴 TODO |

### Queries
| Task | Status |
|------|--------|
| Total usage per customer | 🔴 TODO |
| Expected vs billed totals | 🔴 TODO |

### Tests
| Task | Status |
|------|--------|
| Integration tests base | 🔴 TODO |
| Tests per endpoint | 🔴 TODO |
| Idempotency test | 🔴 TODO |

---

## Interview Readiness

| Question | Phase | Ready? |
|----------|-------|--------|
| Design usage-based billing data model | 1 | 🟡 (can explain model) |
| How to prevent duplicate billing events? | 1 | 🟡 (know concept) |
| SQL: calculate total usage per customer | 1 | 🔴 |
| How to detect revenue leakage? | 2 | 🔴 |
| Design a reconciliation system | 2 | 🔴 |
| How to handle failed batch jobs? | 2 | 🔴 |

---

## Weekly Log

### Week 1: 21-27 March 2026

**Done:**
- [x] Created RevRecon repo
- [x] README.md, LICENSE
- [x] AGENTS.md, CLAUDE.md
- [x] .context/ structure + private files
- [x] docs/ structure (adr, architecture, domain)
- [x] Consilium: 5-phase plan validated
- [x] ADR-001: Layered Architecture decision
- [x] Spring Boot project setup (4.0.4, Java 21)
- [x] PostgreSQL + Flyway configuration
- [x] Package structure (controller, service, repository, model)

**Learned:**
- Layered vs Clean vs Hexagonal: trade-offs
- Why start simple: "can't understand complex until you feel pain from simple"
- MVC vs Layered for backend APIs
- Refactor triggers: CLI, second data source, painful tests

---

### Week 2: 28 March 2026

**Done:**
- [x] Data model discussion (entities, relationships)
- [x] V1__init.sql migration written
- [x] 5 tables: customers, plans, subscriptions, usage_events, billing_records
- [x] 2 ENUMs: subscription_status, billing_status
- [x] Domain glossary updated

**Learned:**
- Idempotency key: UNIQUE constraint vs primary key
- JSONB for flexible pricing (Map<metric, price>)
- ENUM in PostgreSQL: CREATE TYPE ... AS ENUM
- TIMESTAMPTZ vs TIMESTAMP (timezone matters)
- Why migrations: versioning for database schema

**Next:**
- [ ] Java entities matching the schema
- [ ] Repositories
- [ ] First endpoint (POST /events)

---

**Last Updated:** 28 March 2026
