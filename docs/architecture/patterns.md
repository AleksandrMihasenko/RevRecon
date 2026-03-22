# Pattern Map

**Last Updated:** 22 March 2026  
**Purpose:** Document patterns used in RevRecon — where, why, and what alternatives exist.

---

## How to Use This Document

For each pattern:
1. **Name** — what pattern
2. **Where** — location in code
3. **Problem** — what problem it solves
4. **Why chosen** — why this pattern over alternatives
5. **Alternative** — what else could work, trade-off
6. **Learned** — what I understood from using it

---

## Patterns in Use

(To be filled as patterns are implemented)

---

## Patterns to Explore

| Pattern | When to consider | Status |
|---------|------------------|--------|
| Repository | Data access abstraction | 🔴 TODO |
| Service Layer | Business logic isolation | 🔴 TODO |
| DTO | Request/response separation | 🔴 TODO |
| Factory | Object creation | 🔴 TODO |
| Strategy | Interchangeable algorithms | 🔴 TODO |
| Adapter | Interface translation | 🔴 TODO |
| Observer / Events | Decoupled notifications | 🔴 TODO |
| Specification | Complex query building | 🔴 TODO |
| Unit of Work | Transaction management | 🔴 TODO |
| Idempotency Key | Prevent duplicate processing | 🔴 TODO |

---

## Architectural Patterns

| Pattern | Description | Status | ADR |
|---------|-------------|--------|-----|
| Layered | Controller → Service → Repository | 🟡 Current | [ADR-001](../adr/0001-initial-architecture-choice.md) |
| Clean Architecture | Use Cases, Entities, Interfaces | 🔴 Later | — |
| Hexagonal | Ports & Adapters | 🔴 Later | — |
| Event-Driven | Async communication via events | 🔴 Later | — |
| Event Sourcing | Store events, derive state | 🔴 Phase 4 option | — |
| CQRS | Separate read/write models | 🔴 Phase 4 option | — |

**Goal:** Try at least 2-3 approaches, compare, document trade-offs.

---

## Pattern Comparison Notes

### Layered vs Clean vs Hexagonal

**Context:** ADR-001 — choosing initial architecture for Phase 1.

**Layered pros:**
- Simple to understand
- Fast to get started
- Good enough for MVP

**Clean/Hexagonal pros:**
- Domain isolated from infrastructure
- Better testability
- Easy to swap implementations

**Decision:** Layered for Phase 1. Refactor when pain felt.

**What I learned:** 
- "Can't understand why complex is needed until you feel pain from simple"
- Refactor triggers: adding CLI, second data source, painful tests, logic leaking to controller

**Full discussion:** [ADR-001](../adr/0001-initial-architecture-choice.md)

---

## References

- "Patterns of Enterprise Application Architecture" — Martin Fowler
- "Clean Architecture" — Robert C. Martin
- "Domain-Driven Design" — Eric Evans
