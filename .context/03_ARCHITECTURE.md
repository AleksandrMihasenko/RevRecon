# Architecture

**Status:** Phase 1 closed. Phase 2 reconciliation has started with the first dynamic discrepancy rule.

---

## Planned Components
```
┌─────────────────────────────────────────────────┐
│                   RevRecon                      │
├─────────────────────────────────────────────────┤
│  API Layer                                      │
│  ├── POST /usage-events (usage ingestion)       │
│  ├── POST /billing-records (billing ingestion)  │
│  ├── GET /usage-billing-summary                 │
│  ├── GET /health                                │
│  ├── GET /discrepancies                         │
│  └── GET /discrepancies/{id}/explain            │
├─────────────────────────────────────────────────┤
│  Service Layer                                  │
│  ├── UsageEventService                          │
│  ├── BillingRecordService                       │
│  ├── UsageBillingSummaryService                 │
│  ├── DiscrepancyService                         │
│  ├── ReconciliationService (planned)            │
│  └── DiscrepancyDetector (planned)              │
├─────────────────────────────────────────────────┤
│  Data Layer                                     │
│  ├── UsageEventRepository                       │
│  ├── BillingRecordRepository                    │
│  └── UsageBillingSummaryQuery                   │
├─────────────────────────────────────────────────┤
│  Database (PostgreSQL)                          │
│  ├── usage_events                               │
│  ├── billing_records                            │
│  ├── customers                                  │
│  ├── plans                                      │
│  └── future discrepancy/reconciliation tables   │
└─────────────────────────────────────────────────┘
```

---

## Current Data Model

Current implemented entities:
- Customer
- Plan
- UsageEvent
- BillingRecord
- Discrepancy
- DiscrepancyType

Planned entities:
- ReconciliationSnapshot
- ReconciliationResult

Current architecture direction:
- Usage events and billing records are ingested independently
- Summary endpoint compares usage and billing data dynamically
- `DiscrepancyService` currently derives the first discrepancy dynamically from usage and billing source data
- Future reconciliation layer will expand detection to duplicate billing, wrong pricing, timing mismatches, and richer explanations
- System is intentionally evolving toward correctness/reliability engineering rather than only CRUD operations

Current implemented discrepancy rule:

```text
UNBILLED_USAGE =
usage totals exist for customer + period
and no billing record exists for the exact same customer + period.
```

Current persistence decision:
- Discrepancies are not stored in a table yet.
- They are derived from `usage_events` and `billing_records` at service level.
- A table can be added later if reconciliation runs need history, resolution state, assignment, or audit workflow.

---

## ADRs

| ADR | Decision | Status |
|-----|----------|------|
| ADR-001 | Initial architecture choice: layered architecture for Phase 1 | Accepted |
| ADR-002 | Retry protection and duplicate prevention with idempotency keys and 409 Conflict | Accepted |

---

## Runtime / Local Deployment

Current local runtime uses Docker Compose.

```text
Docker Compose
├── backend container
│   ├── Spring Boot application
│   ├── built from backend/Dockerfile
│   └── connects to PostgreSQL through Docker network
│
└── postgres container
    ├── postgres:16 image
    └── named Docker volume for persistent local data
```

Key runtime decisions:
- Backend and PostgreSQL run in separate containers because they have different responsibilities and lifecycles
- Backend is treated as stateless application runtime
- PostgreSQL is stateful and stores data in a named Docker volume
- Backend connects to PostgreSQL using the Compose service name `postgres`
- Local env values are configured through environment variables and documented in `deploy/env/.env.example`
- Backend exposes `GET /api/health` as a lightweight liveness check
- The health endpoint does not check PostgreSQL yet; DB readiness can be added later if deployment needs it

Current cleanup command:

```bash
cd deploy

docker-compose down -v
```

---

**Last Updated:** 17 May 2026
