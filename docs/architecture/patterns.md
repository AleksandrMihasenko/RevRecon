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

### 1. [Pattern Name]

**Where:** `path/to/code`

**Problem:** What problem does it solve?

**Why chosen:** Why this approach?

**Alternative:** What else could work? Trade-off?

**Learned:** What did I understand?

---

### 2. [Pattern Name]

(template repeats)

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

---

## Architectural Patterns to Try

| Pattern | Description | Status |
|---------|-------------|--------|
| Layered (MVC) | Controller → Service → Repository | 🔴 TODO |
| Clean Architecture | Use Cases, Entities, Interfaces | 🔴 TODO |
| Hexagonal | Ports & Adapters | 🔴 TODO |
| Event-Driven | Async communication via events | 🔴 TODO |
| Event Sourcing | Store events, derive state | 🔴 TODO |
| CQRS | Separate read/write models | 🔴 TODO |

**Goal:** Try at least 2-3 approaches, compare, document trade-offs.

---

## Pattern Comparison Notes

### [Pattern A] vs [Pattern B]

**Context:** When did this comparison come up?

**Pattern A pros:**
- ...

**Pattern B pros:**
- ...

**Decision:** Which did I choose and why?

**What I learned:** ...

---

## References

- "Patterns of Enterprise Application Architecture" — Martin Fowler
- "Clean Architecture" — Robert C. Martin
- "Domain-Driven Design" — Eric Evans
