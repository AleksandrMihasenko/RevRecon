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
| Repositories | ✅ |
| Exception hierarchy | ✅ |
| **POST /usage-events** | ✅ |
| UsageEvent validation + error handling | ✅ |
| UsageEvent controller tests | ✅ |
| POST /billing | 🟡 In Progress |

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
| Package structure (controller, service, repository, model, exception, dto) | ✅ |

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

### DTOs ✅ DONE
| Task | Status |
|------|--------|
| UsageEventRequest | ✅ |
| UsageEventResponse | ✅ |
| UsageEventErrorResponse | ✅ |
| BillingRecordRequest | ✅ |
| BillingRecordResponse | ✅ |

### API 🟡 IN PROGRESS
| Task | Status |
|------|--------|
| POST /api/usage-events | ✅ |
| 201 Created response | ✅ |
| 409 Conflict on duplicate | ✅ |
| DTO validation on POST /api/usage-events | ✅ |
| Global exception handler for usage ingestion | ✅ |
| Structured error responses for 400/409 | ✅ |
| POST /billing (billing records) | 🟡 In Progress |
| Billing idempotency key design (ADR-0002) | ✅ |
| Billing DTO validation | ✅ |
| Billing controller tests skeleton | ✅ |
| Billing invalid period error path | 🟡 In Progress |

### Queries
| Task | Status |
|------|--------|
| Total usage per customer | 🔴 TODO |
| Expected vs billed totals | 🔴 TODO |

### Tests
| Task | Status |
|------|--------|
| Integration tests base | 🔴 TODO |
| Controller tests for POST /api/usage-events | ✅ |
| Idempotency test (controller level) | ✅ |
| Controller tests for POST /api/billing | 🟡 In Progress |
| Integration tests per endpoint | 🔴 TODO |

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
| How to distinguish retry identity vs business identity? | 1 | ✅ (can explain) |
| SQL: calculate total usage per customer | 1 | 🔴 |
| INSERT vs UPDATE separation in billing | 1 | ✅ (can explain why) |
| Exception handling strategy | 1 | ✅ (domain vs technical) |
| DTO vs Entity — why separate? | 1 | ✅ (can explain) |
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

### Week 3: 4-5 April 2026

**Done:**
- [x] All 5 repositories completed (raw JDBC)
- [x] Exception hierarchy (RevReconException, DuplicateEventException)
- [x] Idempotency handling in UsageEventRepository
- [x] Useful query methods: findByCustomerIdAndPeriod, updateStatus
- [x] UsageEventRequest DTO
- [x] UsageEventResponse DTO
- [x] UsageEventService
- [x] UsageEventController
- [x] POST /api/usage-events endpoint (201 + 409)

**Learned:**
- Named parameters (:name) vs positional (?) — named safer for complex queries
- Enum conversion: toUpperCase() for DB → Java, toLowerCase() for Java → DB
- NULL handling in JDBC: check before toInstant()
- Exception responsibility: Repository converts technical → domain exceptions
- Base exception class: allows unified error handling at controller level
- Partial update methods (updateStatus) vs full update — safer for billing
- @Data (Lombok): generates getters, setters, toString, equals, hashCode
- DTO vs Entity: separate contracts for API and DB
- Request DTO vs Response DTO: different fields (no id in request, all fields in response)
- URL naming: kebab-case (/usage-events), not camelCase
- RETURNING * in PostgreSQL: get inserted row in one query
- ResponseEntity for HTTP codes (201 Created, 409 Conflict)
- Lombok @Data creates void setters — can't chain, use constructor or separate calls

**Next:**
- [ ] Run and test POST /usage-events
- [ ] POST /billing endpoint
- [ ] Integration tests

### Week 5: 21-25 April 2026

**Done:**
- [x] Clarified UsageEvent domain rules in docs
- [x] Added DTO validation to UsageEventRequest
- [x] Added UsageEventErrorResponse DTO
- [x] Moved duplicate handling to GlobalExceptionHandler
- [x] Added validation error handling for 400 responses
- [x] Added controller tests for POST /api/usage-events
- [x] Covered 3 scenarios: 201 Created, 400 Bad Request, 409 Conflict

**Learned:**
- Request validation belongs at the controller boundary, not in the service
- Response DTOs should not carry request-validation annotations
- Global exception handling keeps controller success flow clean
- In controller tests, mock the service, not the repository
- Invalid requests should fail before the service layer is called

### Week 6: 26 April 2026

**Done:**
- [x] ADR-0002: retry protection and duplicate prevention choice
- [x] Decided billing ingestion uses caller-generated `idempotencyKey`
- [x] Chose `409 Conflict` on duplicate billing idempotency key
- [x] Added `idempotency_key` to `billing_records` schema design
- [x] Added `BillingRecordRequest` and `BillingRecordResponse`
- [x] Added `BillingController` and `BillingRecordService` skeletons
- [x] Aligned `BillingRecordRepository.insert()` with `RETURNING *`
- [x] Added controller tests for billing scenarios: 201, 400 validation, 409 duplicate, 400 invalid period
- [x] Added `InvalidBillingPeriodException`
- [x] Added handler path for invalid billing period

**Refactoring / cleanup next:**
- [ ] Normalize error DTO naming (`UsageEventErrorResponse` vs billing-specific error response)
- [ ] Decide whether to keep one generic API error DTO across endpoints
- [ ] Wire real period validation in `BillingRecordService`
- [ ] Finish billing endpoint implementation until controller tests pass end-to-end
- [ ] Consider renaming `DuplicateEventException` to a more generic duplicate-ingestion exception later

**Learned:**
- Request shape validation and business-rule validation are different concerns
- Duplicate request handling and invalid period handling must be tested as separate paths
- Service-exception tests need fully valid JSON so controller validation does not short-circuit them
- Repository mapping must stay aligned with domain constructor order, especially after adding fields like `idempotencyKey`

---

**Last Updated:** 26 April 2026
