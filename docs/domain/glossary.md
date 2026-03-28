# Domain Glossary

**Last Updated:** 28 March 2026
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

**Definition:** A single unit of billable activity.

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

---

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

---

## Reconciliation Flow

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

---

## Notes & Insights

(Add as learning progresses)
