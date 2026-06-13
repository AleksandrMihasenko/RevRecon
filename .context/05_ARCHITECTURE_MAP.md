# Architecture Docs

**Updated:** 17 May 2026

---

## Purpose

- Keep architecture understanding explicit and versioned
- Track technical debt as small, executable steps
- Document important design decisions

---

## Where to Find What

| Topic | Location |
|-------|----------|
| Architecture overview | [docs/architecture/overview.md](../docs/architecture/overview.md) |
| Pattern map | [docs/architecture/patterns.md](../docs/architecture/patterns.md) |
| ADRs | [docs/ADR/](../docs/ADR/) |
| Domain glossary | [docs/domain/glossary.md](../docs/domain/glossary.md) |
| Diagrams | [docs/architecture/diagrams/](../docs/architecture/diagrams/) |

---

## Key Decisions

| ADR | Decision | Status |
|-----|----------|--------|
| [ADR-001](../docs/ADR/ADR-0001-initial-architecture-choice.md) | Layered Architecture for Phase 1 | ✅ Accepted |

---

## Current Architecture

**Style:** Layered (Controller → Service → Repository)

**Package structure:**
```
com.revrecon.backend/
├── controller/
│   ├── UsageEventController.java
│   ├── BillingController.java
│   ├── UsageBillingSummaryController.java
│   └── HealthController.java
├── service/
│   ├── UsageEventService.java
│   ├── BillingRecordService.java
│   ├── UsageBillingSummaryService.java
│   └── DiscrepancyService.java
├── repository/
│   ├── CustomerRepository.java
│   ├── PlanRepository.java
│   ├── SubscriptionRepository.java
│   ├── UsageEventRepository.java
│   └── BillingRecordRepository.java
├── model/
│   ├── Customer.java
│   ├── Plan.java
│   ├── Subscription.java
│   ├── SubscriptionStatus.java
│   ├── UsageEvent.java
│   ├── UsageMetricTotal.java
│   ├── BillingRecord.java
│   ├── BillingRecordStatus.java
│   ├── Discrepancy.java
│   └── DiscrepancyType.java
├── dto/
│   ├── UsageEventRequest.java
│   ├── UsageEventResponse.java
│   ├── UsageEventErrorResponse.java
│   ├── BillingRecordRequest.java
│   ├── BillingRecordResponse.java
│   ├── BillingRecordErrorResponse.java
│   ├── UsageBillingSummaryResponse.java
│   ├── UsageByMetricResponse.java
│   └── HealthResponse.java
├── exception/
│   ├── RevReconException.java
│   ├── DuplicateEventException.java
│   └── InvalidBillingPeriodException.java
├── handler/
│   └── GlobalExceptionHandler.java
└── BackendApplication.java

src/test/java/com/revrecon/backend/
├── controller/
│   ├── UsageEventControllerTest.java
│   ├── BillingControllerTest.java
│   ├── UsageBillingSummaryControllerTest.java
│   └── HealthControllerTest.java
└── service/
    ├── BillingRecordServiceTest.java
    ├── UsageBillingSummaryServiceTest.java
    └── DiscrepancyServiceTest.java
```

**Data flow:**
```
HTTP Request → Controller → Service → Repository → PostgreSQL
                  ↓              ↓           ↓
              DTO (Request)   Entity    Raw JDBC + SQL
                  ↑              ↑           ↑
HTTP Response ← Controller ← Service ← Repository
                  ↓
              DTO (Response)
```

**Current reconciliation flow:**
```
DiscrepancyService
    → UsageEventRepository.getUsageTotalsByMetric(customerId, periodStart, periodEnd)
    → BillingRecordRepository.findByCustomerIdAndPeriod(customerId, periodStart, periodEnd)
    → if usage exists and billing record is missing:
          Discrepancy(type = UNBILLED_USAGE)
```

Current rule boundary:
- `UNBILLED_USAGE` means usage exists, but the billing record is missing.
- Billing exists but usage is missing is a separate future discrepancy type.
- Discrepancies are derived dynamically and are not stored yet.

**Exception flow:**
```
Validation error (invalid request body)
    → MethodArgumentNotValidException (Spring)
        → GlobalExceptionHandler → 400 Bad Request (UsageEventErrorResponse)

PostgreSQL constraint violation
    → DuplicateKeyException (Spring)
        → DuplicateEventException (Domain, caught in Repository)
            → GlobalExceptionHandler → 409 Conflict (UsageEventErrorResponse)

Billing period business rule violation
    → InvalidBillingPeriodException (Domain, thrown in Service)
        → GlobalExceptionHandler → 400 Bad Request (BillingRecordErrorResponse)

Summary period business rule violation
    → InvalidBillingPeriodException (Domain, thrown in Service)
        → GlobalExceptionHandler → 400 Bad Request
```

**Refactor triggers** (when to reconsider architecture):
- Adding CLI or second interface
- Changing data source
- Unit tests become painful
- Business logic leaks to Controller

## Current Focus

- Phase 1 is closed: ingestion, summary, local runtime baseline, and health check are in place
- Phase 2 started with one concrete reconciliation scenario before designing every discrepancy type
- The complete usage/billing presence matrix is covered by service tests
- `GET /api/discrepancies` exposes results through a response DTO
- Invalid periods are rejected before repository calls
- Next step: refine the explanation and add PostgreSQL integration coverage
- Keep larger architectural changes delayed until Phase 2 creates real pain
