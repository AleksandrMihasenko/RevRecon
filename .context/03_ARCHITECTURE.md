# Architecture

**Status:** TBD — will be defined as implementation progresses.

---

## Planned Components
```
┌─────────────────────────────────────────────────┐
│                   RevRecon                       │
├─────────────────────────────────────────────────┤
│  API Layer                                       │
│  ├── POST /events (usage ingestion)             │
│  ├── POST /billing (billing records)            │
│  ├── GET /discrepancies                         │
│  └── GET /discrepancies/{id}/explain            │
├─────────────────────────────────────────────────┤
│  Service Layer                                   │
│  ├── ReconciliationService                      │
│  ├── DiscrepancyDetector                        │
│  └── ExplainabilityEngine                       │
├─────────────────────────────────────────────────┤
│  Data Layer                                      │
│  ├── UsageEventRepository                       │
│  ├── BillingRecordRepository                    │
│  └── DiscrepancyRepository                      │
├─────────────────────────────────────────────────┤
│  Database (PostgreSQL)                           │
│  ├── usage_events                               │
│  ├── billing_records                            │
│  ├── customers                                  │
│  ├── plans                                      │
│  └── discrepancies                              │
└─────────────────────────────────────────────────┘
```

---

## Data Model (Draft)

**Will be defined in Phase 1.**

Core entities:
- Customer
- Plan
- UsageEvent
- BillingRecord
- Discrepancy

---

## ADRs

| ADR | Decision | Date |
|-----|----------|------|
| ADR-001 | TBD | ... |

---

**Last Updated:** 22 March 2026
