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
| Data model (V1 migration) | ✅ |
| Java entities | ✅ |
| **Repositories** | ✅ |
| Exception hierarchy | ✅ |
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
| Package structure (controller, service, repository, model, exception) | ✅ |

### Data Model ✅ DONE
| Task | Status |
|------|--------|
| V1__init.sql migration | ✅ |
| customers table | ✅ |
| plans table (JSONB prices) | ✅ |
| subscriptions table (ENUM status) | ✅ |
| usage_events table (idempotency_key UNIQUE) | ✅ |
| billing_records table (ENUM status) | ✅ |

### Entities (Java) ✅ DONE
| Task | Status |
|------|--------|
| Customer entity | ✅ |
| Plan entity | ✅ |
| Subscription entity | ✅ |
| SubscriptionStatus enum | ✅ |
| UsageEvent entity | ✅ |
| BillingRecord entity | ✅ |
| BillingRecordStatus enum | ✅ |

### Repositories ✅ DONE
| Task | Status |
|------|--------|
| CustomerRepository (raw JDBC) | ✅ |
| PlanRepository | ✅ |
| SubscriptionRepository | ✅ |
| UsageEventRepository | ✅ |
| BillingRecordRepository | ✅ |

**Decision:** Using raw JDBC (NamedParameterJdbcTemplate) to understand boilerplate and trade-offs.

### Exception Handling ✅ DONE
| Task | Status |
|------|--------|
| RevReconException (base) | ✅ |
| DuplicateEventException | ✅ |

### API 🔴 TODO
| Task | Status |
|------|--------|
| POST /events (usage ingestion) | 🔴 TODO |
| POST /billing (billing records) | 🔴 TODO |
| Idempotency handling (409 Conflict) | 🔴 TODO |

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

## Phase 2 Preview

| Task | Status |
|------|--------|
| Optimistic locking (version column) | 🔴 TODO |
| V2__add_version.sql migration | 🔴 TODO |
| update() with version check | 🔴 TODO |

---

## Interview Readiness

| Question | Phase | Ready? |
|----------|-------|--------|
| Design usage-based billing data model | 1 | ✅ (can explain) |
| How to prevent duplicate billing events? | 1 | ✅ (idempotency_key + 409) |
| SQL: calculate total usage per customer | 1 | 🔴 |
| INSERT vs UPDATE separation in billing | 1 | ✅ (can explain why) |
| Exception handling strategy | 1 | ✅ (domain vs technical) |
| Optimistic locking | 2 | 🟡 (know concept) |
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

### Week 2: 28-29 March 2026

**Done:**
- [x] Data model discussion (entities, relationships)
- [x] V1__init.sql migration written
- [x] 5 tables: customers, plans, subscriptions, usage_events, billing_records
- [x] 2 ENUMs: subscription_status, billing_status
- [x] Domain glossary updated
- [x] All Java entities created (7 files)
- [x] CustomerRepository (raw JDBC)

**Learned:**
- Idempotency key: UNIQUE constraint, return 409 on duplicate
- JSONB for flexible pricing — store as String, parse in service
- ENUM in PostgreSQL + Java enum for type safety
- TIMESTAMPTZ → Instant (not LocalDateTime)
- Spring Data JDBC vs raw JDBC trade-offs
- INSERT vs UPDATE: separate methods in billing (clarity > convenience)
- Optimistic locking: version column, WHERE id=? AND version=?
- Partial vs Full update problem

---

### Week 3: 4 April 2026

**Done:**
- [x] All 5 repositories completed (raw JDBC)
- [x] Exception hierarchy (RevReconException, DuplicateEventException)
- [x] Idempotency handling in UsageEventRepository
- [x] Useful query methods: findByCustomerIdAndPeriod, updateStatus

**Learned:**
- Named parameters (:name) vs positional (?) — named safer for complex queries
- Enum conversion: toUpperCase() for DB → Java, toLowerCase() for Java → DB
- NULL handling in JDBC: check before toInstant()
- Exception responsibility: Repository converts technical → domain exceptions
- Base exception class: allows unified error handling at controller level
- Partial update methods (updateStatus) vs full update — safer for billing

**Next:**
- [ ] POST /events endpoint
- [ ] POST /billing endpoint
- [ ] Service layer
- [ ] 409 Conflict response handling

---

**Last Updated:** 4 April 2026
