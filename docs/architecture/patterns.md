# Pattern Map

**Last Updated:** 17 May 2026  
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

### Service Layer

**Where:** `UsageEventService`, `BillingRecordService`, `UsageBillingSummaryService`, `DiscrepancyService`

**Problem:** Business rules should not live in controllers or repositories.

**Why chosen:** The current architecture is layered. Services are the right place to orchestrate repositories and apply use-case/business rules.

**Alternative:** Move rules into separate domain rule classes. This is useful later if discrepancy rules grow and one service turns into a long chain of unrelated `if` statements.

**Learned:** The first discrepancy rule is readable because the business language appears in code names: `Discrepancy`, `DiscrepancyType`, `UNBILLED_USAGE`, and the service test scenario.

### Derived Domain Object

**Where:** `Discrepancy`

**Problem:** The system needs to return a business result without necessarily storing it in a table.

**Why chosen:** The first reconciliation rule can be calculated from existing `usage_events` and `billing_records`. Persisting discrepancies now would add workflow assumptions that do not exist yet.

**Alternative:** Persist discrepancies in a dedicated table when the product needs reconciliation run history, resolution status, assignment, or audit workflow.

**Learned:** A model class is not always a database entity. In this case, `Discrepancy` represents a derived business finding.

---

## Patterns to Explore

| Pattern | When to consider | Status |
|---------|------------------|--------|
| Repository | Data access abstraction | 🔴 TODO |
| Service Layer | Business logic isolation | ✅ In use |
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
| Layered | Controller → Service → Repository | 🟡 Current | [ADR-001](../ADR/ADR-0001-initial-architecture-choice.md) |
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

**Full discussion:** [ADR-001](../ADR/ADR-0001-initial-architecture-choice.md)

---

## References

- "Patterns of Enterprise Application Architecture" — Martin Fowler
- "Clean Architecture" — Robert C. Martin
- "Domain-Driven Design" — Eric Evans
