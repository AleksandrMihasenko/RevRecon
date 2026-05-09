# Architecture

**Status:** Phase 1 runtime and ingestion flow implemented. Reconciliation/discrepancy detection architecture still evolving.

---

## Planned Components
```
┌─────────────────────────────────────────────────┐
│                   RevRecon                      │
├─────────────────────────────────────────────────┤
│  API Layer                                      │
│  ├── POST /usage-events (usage ingestion)       │
│  ├── POST /billing-records (billing ingestion)  │
│  │  ├── GET /usage-billing-summary              │
│  ├── GET /discrepancies                         │
│  └── GET /discrepancies/{id}/explain            │
├─────────────────────────────────────────────────┤
│  Service Layer                                  │
│  ├── UsageEventService                          │
│  ├── BillingRecordService                       │
│  ├── UsageBillingSummaryService                 │
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

Planned entities:
- Discrepancy
- ReconciliationSnapshot
- ReconciliationResult

Current architecture direction:
- Usage events and billing records are ingested independently
- Summary endpoint compares usage and billing data dynamically
- Future reconciliation layer will detect inconsistencies, missing charges, duplicate billing, and timing mismatches
- System is intentionally evolving toward correctness/reliability engineering rather than only CRUD operations

---

## ADRs

| ADR | Decision | Status |
|-----|----------|------|
| ADR-001 | Use PostgreSQL as primary relational datastore | Accepted |
| ADR-002 | Use JDBC repositories instead of ORM for learning lower-level DB interaction | Accepted |
| ADR-003 | Separate backend and PostgreSQL into independent Docker containers | Accepted |
| ADR-004 | Use Docker Compose for local runtime orchestration | Accepted |
| ADR-005 | Treat backend runtime as stateless and PostgreSQL as stateful | Accepted |

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

Current cleanup command:

```bash
cd deploy

docker-compose down -v
```

---

**Last Updated:** 9 May 2026
