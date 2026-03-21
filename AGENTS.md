# RevRecon Development Guide

## Goal

Detect where usage and billing don’t match and explain why.

---

## Scope

Focus on:
- usage → billing reconciliation
- discrepancy detection
- explainability of mismatches

Out of scope:
- full billing system
- payment processing
- complex UI

---

## Working approach

Work in small, incremental steps.

For each change:
1. Start from a concrete scenario (realistic failure case)
2. Define expected behavior
3. Implement minimal solution
4. Validate with tests
5. Refine if needed

Avoid building generic abstractions too early.

---

## Tech stack

- Java 17+ (Spring Boot) — core logic
- Python — data processing, analytics (later)
- PostgreSQL — storage

## Core scenarios

The system should support:

1. Missing usage  
   Usage exists but is not billed

2. Duplicated usage  
   Same usage counted multiple times

3. Wrong pricing  
   Usage billed with incorrect pricing rules

4. Timing mismatch  
   Usage assigned to the wrong billing period

---

## Design principles

- Prefer explicit domain models over generic abstractions
- Optimize for correctness, not performance (early stage)
- Keep logic easy to trace and debug
- Make discrepancies explainable, not just detectable

---

## Testing

- Use scenario-based tests
- Each discrepancy type should have:
    - a minimal failing case
    - a passing case after implementation

---

## Code guidelines

- Keep functions small and focused
- Name things based on domain (usage, billing, invoice, discrepancy)
- Avoid premature optimization
- Avoid over-engineering

---

## Iteration strategy

Build in layers:

1. Basic reconciliation (usage vs billed)
2. Discrepancy detection
3. Explanation layer
4. Alerts / simulation (later)

---

## Agent behavior

- Explain approach before writing code
- Ask: "guidance or implementation?" when unclear
- Do not generate full implementations by default
- Prefer teaching over doing
- Show structure and direction, not ready-to-copy code

## Notes

This project is intended to explore how billing systems behave under real-world conditions and where they break.
