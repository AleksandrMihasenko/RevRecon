# Project Progress

**Project:** RevRecon
**Started:** 21 March 2026
**Current Phase:** Phase 2 started — first reconciliation rule implemented
**Market entry:** June 2026

---

## Priorities

| Phase | Priority | Status |
|-------|----------|--------|
| Phase 1-2 | P0 MUST HAVE | ✅ Phase 1 closed, Phase 2 started |
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
| POST /billing | ✅ |
| GET /usage-billing-summary | ✅ |
| Phase 1 controller/service tests | ✅ |
| Local Docker Compose baseline | ✅ |
| GET /api/health | ✅ |
| First discrepancy rule: UNBILLED_USAGE | ✅ |

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
| UsageBillingSummaryResponse | ✅ |
| UsageByMetricResponse | ✅ |
| HealthResponse | ✅ |

### API ✅ DONE
| Task | Status |
|------|--------|
| POST /api/usage-events | ✅ |
| 201 Created response | ✅ |
| 409 Conflict on duplicate | ✅ |
| DTO validation on POST /api/usage-events | ✅ |
| Global exception handler for usage ingestion | ✅ |
| Structured error responses for 400/409 | ✅ |
| POST /api/billing (billing records) | ✅ |
| Billing idempotency key design (ADR-0002) | ✅ |
| Billing DTO validation | ✅ |
| Billing controller tests | ✅ |
| Billing invalid period error path | ✅ |
| Billing service unit tests | ✅ |
| GET /api/usage-billing-summary | ✅ |
| UsageBillingSummaryService | ✅ |
| GET /api/health | ✅ |
| Health endpoint controller test | ✅ |

### Queries
| Task | Status |
|------|--------|
| Usage totals by metric for customer + period | ✅ |
| Billed total for customer + exact period | ✅ |
| Combined usage/billing summary read path | ✅ |
| Expected vs billed totals | 🔴 TODO |

### Tests
| Task | Status |
|------|--------|
| BillingRecordService unit tests | ✅ |
| UsageBillingSummary controller test | ✅ |
| UsageBillingSummary service unit test | ✅ |
| Integration tests base | 🔴 TODO — moved to deploy prep / Phase 2 follow-up |
| Controller tests for POST /api/usage-events | ✅ |
| Idempotency test (controller level) | ✅ |
| Controller tests for POST /api/billing | ✅ |
| Integration tests per endpoint | 🔴 TODO — moved to deploy prep / Phase 2 follow-up |

---

## Phase 1 Closure

**Closed:** 14 May 2026

Phase 1 is closed because the project can:
- accept usage events and billing records through API endpoints
- reject duplicate ingestion requests with explicit 409 responses
- validate request bodies and billing periods
- return a usage/billing summary for a customer period
- run locally through Docker Compose with backend + PostgreSQL
- expose a lightweight liveness endpoint at `GET /api/health`
- verify current controller/service behavior with tests

Remaining items are intentionally follow-up work, not Phase 1 blockers:
- TestContainers integration tests for SQL-backed ingestion and summary flows
- Docker Compose manual check for `GET /api/health`
- hosted deployment direction decision
- optional Spring Actuator or DB readiness checks later

Next product/learning focus: continue Phase 2 reconciliation from the first concrete discrepancy scenario.

---

## Phase 2: Reconciliation + Detection

**Started:** 17 May 2026

### Discrepancy Detection

| Task | Status |
|------|--------|
| Define first scenario: usage exists, billing record missing | ✅ |
| Add `DiscrepancyType.UNBILLED_USAGE` | ✅ |
| Add `Discrepancy` domain model | ✅ |
| Add `DiscrepancyService` service-level rule | ✅ |
| Add service test for unbilled usage | ✅ |
| Negative test: usage exists + billing exists → no discrepancy | 🔴 TODO |
| `GET /api/discrepancies` endpoint | 🔴 TODO |
| Discrepancy response DTO | 🔴 TODO |
| Human-readable explanation refinement | 🔴 TODO |

Current rule:

```text
If a customer has usage totals in a period
and no billing record exists for the exact same customer + period,
return UNBILLED_USAGE.
```

Design note:
- Discrepancies are currently derived dynamically from `usage_events` and `billing_records`.
- No `discrepancies` table exists yet.
- Persisting discrepancies can be considered later if the system needs reconciliation run history, resolution status, owners, or audit workflow.

### Phase 2 Follow-up

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
| SQL: calculate total usage per customer | 1 | ✅ (group by metric + period) |
| INSERT vs UPDATE separation in billing | 1 | ✅ (can explain why) |
| Exception handling strategy | 1 | ✅ (domain vs technical) |
| DTO vs Entity — why separate? | 1 | ✅ (can explain) |
| First revenue leakage rule: unbilled usage | 2 | 🟡 (service rule implemented) |
| Optimistic locking | 2 | 🟡 (know concept) |
| How to detect revenue leakage? | 2 | 🟡 (first concrete case implemented) |
| Design a reconciliation system | 2 | 🔴 |
| How to handle failed batch jobs? | 2 | 🔴 |

---


## Weekly Log

### Reconciliation / Phase 2 Start

**Done:**
- [x] Clarified the project purpose as a reconciliation / revenue leakage detector, not a billing system
- [x] Defined first discrepancy scenario: `UNBILLED_USAGE`
- [x] Chose the narrower definition: usage exists and billing record is missing
- [x] Separated this from the future case where billing exists but no usage supports it
- [x] Added `DiscrepancyType` and `Discrepancy` domain model
- [x] Added `DiscrepancyService`
- [x] Added green service-level test for `UNBILLED_USAGE`

**Learned:**
- Business rules should not live only in comments.
- A rule is made understandable through domain names, enums, small service methods, scenario tests, and docs.
- `UNBILLED_USAGE` means usage exists but a matching billing record is missing.
- Billing exists but usage is missing is a different future discrepancy type, not `UNBILLED_USAGE`.
- For one rule, a simple service-level `if` is enough; rule classes or a rule engine are premature.

**Next:**
- Add negative test: usage exists and billing record exists returns no discrepancies.
- Add `@Service` before wiring the rule into an API endpoint.
- Decide the first API shape for `GET /api/discrepancies`.

### Docker / Local Deploy Prep

**Done:**
- [x] Added backend Dockerfile with multi-stage build
- [x] Added Docker Compose setup for backend + PostgreSQL
- [x] Added environment-variable based configuration for local Docker startup
- [x] Added `.env.example` for local development values
- [x] Verified backend and PostgreSQL start successfully via Docker Compose
- [x] Verified backend is reachable from host machine on `localhost:8080`
- [x] Verified local cleanup flow with `docker-compose down -v`
- [x] Added lightweight health endpoint: `GET /api/health`
- [x] Added `HealthResponse` DTO
- [x] Added controller test for health endpoint

**Learned:**
- Dockerfile builds an image; containers run from images
- Multi-stage builds separate build-time dependencies from runtime dependencies
- Backend container should stay stateless; PostgreSQL is stateful and needs persistent storage
- Docker Compose services communicate by service name inside the Compose network
- `localhost` inside a container refers to the container itself, not another service
- Docker volumes persist database files outside the container lifecycle
- `docker-compose down -v` removes local project containers, network, and volumes

**Next:**
- Keep the current local Docker Compose setup as the baseline runtime
- Verify health endpoint through Docker Compose as a follow-up
- Move toward Phase 2 reconciliation/discrepancy detection


### Week 8: 7 May 2026

**Done:**
- [x] Added controller test for `GET /api/usage-billing-summary`
- [x] Covered query parameters, `200 OK`, usage-by-metric response, and billed total response
- [x] Added `UsageBillingSummaryService` unit test
- [x] Covered orchestration across `UsageEventRepository` and `BillingRecordRepository`
- [x] Verified summary mapping from `UsageMetricTotal` to `UsageByMetricResponse`
- [x] Ran full backend test suite: 13 tests, 0 failures, 0 errors
- [x] Decided TestContainers integration tests are a follow-up for deploy prep / Phase 2, not a blocker for Phase 1 closure

**Learned:**
- Controller tests verify HTTP contract; they do not verify SQL or service orchestration
- Query parameters in `MockMvc` belong inside the request builder via `.param(...)`
- `jsonPath` must come from `MockMvcResultMatchers`, not `MockRestRequestMatchers`
- Service unit tests should keep the service real and mock repositories
- `BigDecimal` assertions should compare against `new BigDecimal("...")`, not floating-point literals

**Environment note:**
- In the Codex sandbox, `mvn test` can fail because Mockito inline mock maker cannot self-attach on Java 24. Running the same command with elevated permissions succeeds.

**Phase 1 checkpoint:**
- Functional scope is complete.
- Controller/service test coverage is complete for current Phase 1 endpoints.
- TestContainers integration tests remain planned as an explicit follow-up before or during deploy prep.

---

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

### Week 7: 3 May 2026

**Done:**
- [x] Added first read-side aggregation queries
- [x] Added `GET /api/usage-billing-summary`
- [x] Added `UsageBillingSummaryService`
- [x] Added `UsageMetricTotal` aggregation model
- [x] Added usage aggregation by metric in `UsageEventRepository`
- [x] Added billed total aggregation in `BillingRecordRepository`
- [x] Compile-checked the new summary flow

**Learned:**
- One `total usage` number is misleading when metrics use different units
- `GROUP BY metric` belongs in SQL, not in Java loops
- `COALESCE(SUM(...), 0)` keeps repository contracts cleaner than leaking `NULL`
- A read-side summary can justify its own service when it orchestrates multiple repositories

**Next at that point:**
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

### Week 6: 1-2 May 2026

**Done:**
- [x] Finished `POST /api/billing` service/controller flow
- [x] Added billing controller coverage for 201, 400, 409, and invalid billing period
- [x] Added `BillingRecordService` unit tests
- [x] Covered valid billing save, duplicate propagation, invalid period rejection, and equal period boundaries
- [x] Refactored service tests with small test-data helpers

**Learned:**
- Service tests should verify business behavior, not HTTP status codes
- In unit tests, mock the dependency and keep the service real
- `assertThrows(...)` is both the action and the assertion for exception paths
- A happy-path service test should validate returned data, not only dependency calls

## Current Next Steps

1. Add negative service test: usage exists + billing record exists → no discrepancy.
2. Add `@Service` to `DiscrepancyService` before wiring it into Spring.
3. Decide first `GET /api/discrepancies` request/response shape.
4. Add controller + DTO only after service behavior is covered.
5. Add TestContainers integration tests as Phase 2 / deploy follow-up.

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

**Last Updated:** 17 May 2026
