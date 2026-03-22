# Project Progress

**Project:** RevRecon
**Started:** 21 March 2026
**Current Phase:** Setup complete → Starting Phase 1
**Market entry:** June 2026

---

## Priorities

| Phase | Priority | Status |
|-------|----------|--------|
| Phase 1-2 | P0 MUST HAVE | 🔴 |
| Phase 3-5 | P1/P2 NICE TO HAVE | 🔴 |

---

## Current Status

| Area | Status |
|------|--------|
| Repository | ✅ |
| README | ✅ |
| License (MIT) | ✅ |
| AGENTS.md, CLAUDE.md | ✅ |
| .context/ structure | ✅ |
| docs/ structure | ✅ |
| Plan validated | ✅ |
| Java project setup | 🔴 TODO |
| ADR-001 | 🔴 TODO |

---

## Phase 1: Core Model + Ingestion

**Target:** April 2026
**Priority:** P0 MUST HAVE

### Setup
| Task | Status |
|------|--------|
| Spring Boot project | 🔴 TODO |
| PostgreSQL + Flyway | 🔴 TODO |
| ADR-001: Layered architecture | 🔴 TODO |

### Entities
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
| Idempotency keys | 🔴 TODO |
| Audit fields (created_at, updated_at) | 🔴 TODO |

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

## Phase 2: Reconciliation + Detection

**Target:** May 2026
**Priority:** P0 MUST HAVE

| Task | Status |
|------|--------|
| Comparison logic: expected vs billed | 🔴 TODO |
| Discrepancy types classification | 🔴 TODO |
| GET /discrepancies | 🔴 TODO |
| Revenue metrics | 🔴 TODO |
| Scheduled reconciliation job | 🔴 TODO |
| Error handling (failed reconciliation) | 🔴 TODO |
| Scenario tests per discrepancy type | 🔴 TODO |

---

## Phase 3-5: Nice to Have

| Phase | Focus | Status |
|-------|-------|--------|
| Phase 3 | Explainability | 🔴 TODO |
| Phase 4 | One advanced experiment (TBD) | 🔴 TODO |
| Phase 5 | Demo readiness | 🔴 TODO |

---

## Interview Readiness

| Question | Phase | Ready? |
|----------|-------|--------|
| Design usage-based billing data model | 1 | 🔴 |
| How to prevent duplicate billing events? | 1 | 🔴 |
| SQL: calculate total usage per customer | 1 | 🔴 |
| How to detect revenue leakage? | 2 | 🔴 |
| Design a reconciliation system | 2 | 🔴 |
| How to handle failed batch jobs? | 2 | 🔴 |
| Explain billing errors to users | 3 | 🔴 |
| Event Sourcing vs CRUD trade-offs | 4 | 🔴 |

---

## Weekly Log

### Week 1: 21-27 March 2026

**Done:**
- [x] Created RevRecon repo
- [x] README.md, LICENSE
- [x] AGENTS.md, CLAUDE.md
- [x] .context/ structure + private files
- [x] docs/ structure (adr, architecture, domain)
- [x] Consilium: plan validated
- [x] Final roadmap with 5 phases

**Learned:**
- Architecture: start Layered, refactor when pain felt
- Production concerns for billing: idempotency, audit
- Phase 1-2 = must have, Phase 3-5 = nice to have

**Next week:**
- [ ] Java project setup (Spring Boot)
- [ ] ADR-001: Layered architecture
- [ ] Data model draft (diagram first)

---

**Last Updated:** 22 March 2026
