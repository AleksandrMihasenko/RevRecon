# Architecture Docs

**Updated:** 22 March 2026

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
| ADRs | [docs/adr/](../docs/adr/) |
| Domain glossary | [docs/domain/glossary.md](../docs/domain/glossary.md) |
| Diagrams | [docs/architecture/diagrams/](../docs/architecture/diagrams/) |

---

## Key Decisions

| ADR | Decision | Status |
|-----|----------|--------|
| [ADR-001](../docs/adr/0001-initial-architecture-choice.md) | Layered Architecture for Phase 1 | ✅ Accepted |

---

## Current Architecture

**Style:** Layered (Controller → Service → Repository)

**Package structure:**
```
com.revrecon.backend/
├── controller/
├── service/
├── repository/
└── model/
```

**Refactor triggers** (when to reconsider):
- Adding CLI or second interface
- Changing data source
- Unit tests become painful
- Business logic leaks to Controller
