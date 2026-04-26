# ADR-0002: Retry protection and duplicate prevention

**Date:** 2026-04-26 
**Status:** Accepted

---

## Context

Billing ingestion needs retry protection and duplicate prevention.

## Problem

- billing writes can be retried because of timeouts or uncertain network failures
- without a retry-protection rule, the same billing request may create duplicate records
- business fields like customer and period are not enough to detect duplicates because multiple legitimate billing records may exist for the same period

## Options Considered

### Option 1: return the original response on a duplicate key

**Description:**
server treats repeated requests with the same idempotency key as the same operation and returns the original successful result.

**Pros:**
- safe retries for clients
- better behavior under network timeouts
- common pattern in mature APIs like Stripe

**Cons:**
- server must store or reconstruct the original response reliably
- more complex to implement correctly
- harder to reason about in an early learning-stage project
- the same key with different payload needs extra validation rules

### Option 2: return 409 Conflict on a duplicate key

**Description:**
server treats repeated requests with the same idempotency key as duplicates and returns 409 Conflict.

**Pros:**
- simple API behavior
- easy to implement and test
- duplicate submissions are explicit and visible
- consistent with existing POST /api/usage-events behavior

**Cons:**
- less friendly for client retries
- clients must handle duplicates as errors
- less convenient in distributed systems where retries are common

### Option 3: no idempotency key, dedupe by business fields

**Description:**
server does not use an explicit retry key and instead tries to infer duplicates from business fields such as customer and billing period.

**Pros:**
- no extra caller-generated retry key needed
- business data looks like the source of truth
- simpler request shape at first glance

**Cons:**
- business fields do not uniquely identify one request
- legitimate similar billing records may be rejected incorrectly
- dedupe rules become fragile and domain-dependent
- mixes retry protection with business identity

## Decision

- caller provides idempotencyKey
- duplicate key returns 409 Conflict

## Rationale

- simplest behavior to implement correctly in Phase 1
- consistent with existing usage ingestion
- easier to reason about during learning
- explicit duplicate detection is useful in a billing-focused system
- chosen intentionally over Stripe-style replay, not by accident

## Consequences

### Positive
- simpler implementation
- consistent with usage-events
- easier to debug and refactor
- easier to keep momentum while learning

### Negative
- clients must treat duplicate retry as an error
- less retry-friendly than Stripe-style replay

### When To Revisit

- replaying the original response may be reconsidered later if the API needs safer client retries

## Related

- `.context/01_PROJECT.md` — Phase 1 goals and deliverables
- `docs/architecture/overview.md` — architecture evolution strategy
- `docs/architecture/patterns.md` — pattern documentation (to be filled)
