# Domain Glossary

**Last Updated:** 17 May 2026
**Purpose:** Ubiquitous language — terms we use in code, conversations, and documentation.

---

## Core Entities

### Customer

**Definition:** An organization or individual who is billed for usage.

**Attributes:**
- `id` — internal identifier
- `name` — customer name
- `created_at`, `updated_at` — audit fields

**Relationships:**
- Has many Subscriptions
- Has many UsageEvents
- Has many BillingRecords

**Example:** "Acme Corp is a Customer with an active subscription to Pro plan."

---

### Plan

**Definition:** A pricing configuration that defines how usage is billed.

**Attributes:**
- `id` — internal identifier
- `name` — plan name (e.g., "Pro", "Enterprise")
- `prices` — JSONB map of metric → price (e.g., `{"api_calls": 0.01, "storage_gb": 0.10}`)
- `created_at`, `updated_at` — audit fields

**Relationships:**
- Has many Subscriptions

**Example:** "Pro Plan charges $0.01 per API call and $0.10 per GB of storage."

---

### Subscription

**Definition:** A Customer's active commitment to a Plan.

**Attributes:**
- `id` — internal identifier
- `customer_id` → Customer
- `plan_id` → Plan
- `discount` — percentage discount (integer: 10 = 10%)
- `start_date` — when subscription started
- `end_date` — when subscription ends (NULL = ongoing)
- `status` — ENUM: `active`, `paused`, `cancelled`, `expired`
- `created_at`, `updated_at` — audit fields

**Relationships:**
- Belongs to Customer
- Belongs to Plan

**Example:** "Acme Corp has an active Subscription to Pro Plan with 10% discount."

---

### UsageEvent

**Definition:** A metering observation received from a source system.

**Attributes:**
- `id` — internal identifier
- `idempotency_key` — unique key from source system (prevents duplicates)
- `customer_id` → Customer
- `metric` — what was used (e.g., "api_calls", "storage_gb")
- `quantity` — how much (DECIMAL for fractional values)
- `timestamp` — when usage occurred
- `created_at`, `updated_at` — audit fields

**Relationships:**
- Belongs to Customer

**Idempotency:** Same `idempotency_key` twice → second insert rejected by UNIQUE constraint.

**Example:** "Customer made 150 API calls on March 22, 2026."

### UsageEvent Quantity

`UsageEvent` represents a metering observation received from a source system, not only confirmed billable consumption.

`quantity` must be non-negative (`>= 0`). A value of `0` is valid and means the source system explicitly reported zero usage. This is different from missing data: zero means
"observed and reported as none", while missing means "no observation was received".

This distinction matters for reconciliation, because explicit zero usage can still help explain billing mismatches.


---

### UsageEvent Mutability

Core `UsageEvent` fields are immutable after creation:
- `idempotency_key`
- `customer_id`
- `metric`
- `quantity`
- `timestamp`

If source data was wrong, the correction should be modeled as a separate flow, not an in-place update. This preserves auditability and keeps the original metering observation
traceable.

### BillingRecord

**Definition:** A calculated charge for a billing period.

**Attributes:**
- `id` — internal identifier
- `customer_id` → Customer
- `period_start` — billing period start
- `period_end` — billing period end
- `amount` — total amount billed (DECIMAL)
- `status` — ENUM: `draft`, `open`, `paid`, `voided`
- `created_at`, `updated_at` — audit fields

**Relationships:**
- Belongs to Customer

**Example:** "March billing for Acme Corp: $17.00, status: paid."

---

### Discrepancy

**Definition:** A detected mismatch between usage data and billing data for a customer and period.

Current attributes:
- `customerId` — customer affected by the mismatch
- `type` — discrepancy classification
- `periodStart` — billing period start
- `periodEnd` — billing period end
- `explanation` — human-readable reason for the mismatch

Current persistence:
- `Discrepancy` is a derived domain object.
- It is not stored in a database table yet.
- It is calculated from `usage_events` and `billing_records`.

**Example:** "Customer has usage in May 2026, but no billing record exists for May 2026."

---

## Enums

### subscription_status

| Value | Meaning |
|-------|---------|
| `active` | Subscription is running |
| `paused` | Temporarily stopped |
| `cancelled` | Customer cancelled |
| `expired` | End date passed |

### billing_status

| Value | Meaning |
|-------|---------|
| `draft` | Not yet finalized |
| `open` | Sent to customer, awaiting payment |
| `paid` | Payment received |
| `voided` | Cancelled/invalid |

### discrepancy_type

| Value | Meaning |
|-------|---------|
| `UNBILLED_USAGE` | Usage exists for the customer and period, but no matching billing record exists |

Important distinction:
- `UNBILLED_USAGE` means usage exists and billing is missing.
- Billing exists but no usage supports it is a separate future discrepancy type, not `UNBILLED_USAGE`.

---

## Reconciliation Flow

### Current Minimal Flow

```
1. Get usage totals for customer + period
2. Get billing record for same customer + exact period
3. If usage exists and billing record is missing → Discrepancy(type = UNBILLED_USAGE)
4. Otherwise → no discrepancy for this rule
```

### Future Pricing Flow

```
1. Get UsageEvents for customer + period
2. Get Subscription → Plan → prices
3. Calculate: expected = SUM(quantity × price per metric)
4. Apply discount if any
5. Compare expected vs BillingRecord.amount
6. If mismatch → Discrepancy (Phase 2)
```

---

## Key Concepts

### Idempotency Key

A unique identifier sent by the source system to prevent duplicate events.

**Why needed:** Network retry → same event sent twice → without idempotency key, we'd bill twice.

**Implementation:** `UNIQUE` constraint on `idempotency_key` column.

### Metric

A string identifier for the type of usage: `api_calls`, `storage_gb`, `seats`, etc.

The unit is embedded in the name (e.g., `storage_gb` not just `storage`).

### Audit Fields

Every table has `created_at` and `updated_at` for tracking when records were created and modified.

### Unbilled Usage

Usage that was recorded by the metering/source system but was not represented by a billing record for the same customer and billing period.

This is the first implemented revenue leakage scenario in RevRecon.

---

## Notes & Insights

### 17 May 2026

**Topic:** First reconciliation rule

**Insight:** The first Phase 2 rule is intentionally narrow: usage exists and the billing record is missing. Amount mismatches and billing-without-usage are separate future discrepancy types.
