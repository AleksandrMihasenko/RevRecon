# ADR-0001: Initial Architecture Choice

**Date:** 2026-03-22  
**Status:** Accepted

---

## Context

RevRecon is an early-stage pet project that should move quickly.

The current goal is to explore real billing discrepancy scenarios and build working functionality around:
- usage ingestion
- billing ingestion
- reconciliation
- discrepancy detection
- explainability

At this stage, the project is still small, exploratory, and built by a single developer.  
The main priority is to ship scenarios fast, learn from them, and keep the codebase structured enough to evolve later.

## Options Considered

### Option 1: Layered Architecture

**Description:** Organize code into horizontal layers with clear responsibilities. Each layer only depends on the layer below it.
```
Controller (HTTP) → Service (Business Logic) → Repository (Data Access)
```

**Pros:**
- Simple to understand and implement
- Fast to get started — minimal boilerplate
- Well-documented, many examples exist
- Good enough for MVP and small-to-medium projects

**Cons:**
- Controller layer tends to grow large with more endpoints
- Service layer knows about Repository — coupling to data layer
- Business logic can leak into Controller or Repository
- Unit testing Service requires mocking Repository
- Hard to reuse logic for different interfaces (e.g., adding CLI later)

### Option 2: Clean Architecture

**Description:** Domain logic at the center, infrastructure at the edges. Dependencies point inward — domain knows nothing about HTTP or database.
```
        ┌─────────────┐
HTTP →  │   Domain    │  ← Database
        │  (pure)     │
        └─────────────┘
```

**Pros:**
- Domain logic is isolated and testable
- Easy to swap infrastructure (database, HTTP framework)
- Business rules don't depend on external concerns
- Better separation of concerns

**Cons:**
- More upfront complexity (interfaces, use cases, boundaries)
- Overkill for a small MVP
- Risk of overengineering before understanding the domain
- Learning curve — easy to do wrong without experience

### Option 3: Hexagonal (Ports & Adapters)

**Description:** Similar to Clean Architecture. Domain exposes "ports" (interfaces), infrastructure provides "adapters" (implementations).

**Pros:**
- Excellent testability
- Very flexible for multiple interfaces (API, CLI, events)
- Domain completely decoupled

**Cons:**
- Highest complexity of the three options
- Significant boilerplate
- Overkill for Phase 1

## Decision

We will use **Layered Architecture** for Phase 1.

## Rationale

Layered architecture is a good fit for the current stage because it:

- allows fast feature delivery
- keeps the code understandable and easy to debug
- it is familiar and practical for a Spring Boot backend
- it still provides enough structures to avoid chaos
- it can evolve later if real complexity appears

This is not a rejection of more advanced architectural styles.  
It is a conscious choice to start simple and optimize for learning, iteration speed, and domain exploration.

**Accepted trade-offs:**
- Controller will eventually get bloated
- Service-Repository coupling will limit flexibility
- Adding CLI or second data source will require refactoring

We accept these trade-offs because they are *future* problems, and solving them now would be premature optimization of architecture.

## Consequences

### Positive
- faster implementation of real scenarios
- lower cognitive overhead
- easier to debug and refactor
- easier to keep momentum while learning

### Negative
- weaker boundaries compared to Hexagonal or Clean Architecture
- possible coupling to framework conventions
- may require restructuring later if the project grows significantly

### Refactor Triggers
This decision should be revisited when:

- services or modules start to strongly intertwine
- the current structure creates real pain
- new integrations or entrypoints increase complexity
- stronger boundaries are needed to isolate domain logic

## Related

- `.context/01_PROJECT.md` — Phase 1 goals and deliverables
- `docs/architecture/overview.md` — architecture evolution strategy
- `docs/architecture/patterns.md` — pattern documentation (to be filled)
