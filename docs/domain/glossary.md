# Domain Glossary

**Last Updated:** 22 March 2026  
**Purpose:** Ubiquitous language — terms we use in code, conversations, and documentation.

---

## How to Use

This is the **single source of truth** for domain terminology.

- Code uses these exact terms (class names, method names, variables)
- Documentation uses these terms
- Conversations use these terms

When adding a term:
1. Define it clearly
2. Show how it relates to other terms
3. Give an example

---

## Core Entities

### Customer

**Definition:** An organization or individual who is billed for usage.

**Attributes:**
- id
- name
- ...

**Relationships:**
- Has many Subscriptions
- Has many UsageEvents
- Has many BillingRecords

**Example:** "Acme Corp is a Customer with 3 active Subscriptions."

---

### Plan

**Definition:** A pricing configuration that defines how usage is billed.

**Attributes:**
- id
- name
- pricing model (flat, usage-based, tiered)
- ...

**Relationships:**
- Has many Subscriptions

**Example:** "Pro Plan charges $0.01 per API call after 10,000 free calls."

---

### Subscription

**Definition:** A Customer's active commitment to a Plan.

**Attributes:**
- id
- customer_id
- plan_id
- status (active, cancelled, paused)
- billing_period (monthly, annual)
- ...

**Relationships:**
- Belongs to Customer
- Belongs to Plan

**Example:** "Acme Corp has an active Subscription to Pro Plan, billed monthly."

---

### UsageEvent

**Definition:** A single unit of billable activity.

**Attributes:**
- id
- customer_id
- event_type (api_call, storage_gb, seat, ...)
- quantity
- timestamp
- ...

**Relationships:**
- Belongs to Customer
- May be included in a BillingRecord

**Example:** "Customer made 150 API calls on March 22, 2026."

---

### BillingRecord

**Definition:** A calculated charge for a billing period.

**Attributes:**
- id
- customer_id
- period_start
- period_end
- amount
- status (draft, invoiced, paid)
- ...

**Relationships:**
- Belongs to Customer
- Aggregates UsageEvents

**Example:** "March billing for Acme Corp: $45.00 based on 4,500 API calls."

---

### Discrepancy

**Definition:** A mismatch between expected and actual billing.

**Attributes:**
- id
- type (missing_usage, duplicate, wrong_price, timing_mismatch)
- severity
- status (detected, investigating, resolved)
- ...

**Relationships:**
- References UsageEvent(s) and/or BillingRecord(s)
- Has Explanation

**Example:** "500 API calls from March 20 are not included in March billing."

---

### Explanation

**Definition:** Root cause analysis for a Discrepancy.

**Attributes:**
- id
- discrepancy_id
- cause_category
- description
- ...

**Example:** "Missing usage caused by event ingestion delay — events arrived after billing cutoff."

---

## Domain Events

| Event | When it occurs | Data |
|-------|----------------|------|
| UsageEventReceived | New usage ingested | customer_id, event_type, quantity, timestamp |
| BillingRecordCreated | Billing calculated | customer_id, period, amount |
| DiscrepancyDetected | Mismatch found | type, severity, affected records |
| DiscrepancyResolved | Issue fixed | resolution_type, notes |

---

## Commands

| Command | What it does | Input |
|---------|--------------|-------|
| IngestUsage | Record new usage event | customer, type, quantity, timestamp |
| CalculateBilling | Generate billing for period | customer, period |
| DetectDiscrepancies | Find mismatches | customer, period |
| ExplainDiscrepancy | Analyze root cause | discrepancy_id |

---

## Queries

| Query | What it returns | Filters |
|-------|-----------------|---------|
| GetUsageForPeriod | Usage events | customer, date range |
| GetBillingHistory | Billing records | customer, date range |
| GetDiscrepancies | Detected issues | customer, status, type |
| GetDiscrepancyDetails | Single discrepancy with explanation | discrepancy_id |

---

## Business Rules

| Rule | Description |
|------|-------------|
| Billing cutoff | Usage after cutoff date goes to next period |
| Duplicate detection | Same event_id within 24h = duplicate |
| ... | ... |

---

## Notes

(Add insights about domain as you learn)

**Date:** ...  
**Topic:** ...  
**Insight:** ...
