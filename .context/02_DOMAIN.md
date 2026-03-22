# Domain Knowledge: Billing & Revenue

Learning as I build. Adding terms and concepts here.

---

## Revenue Metrics

| Term | Definition | Notes |
|------|------------|-------|
| **MRR** | Monthly Recurring Revenue | Predictable monthly income from subscriptions |
| **ARR** | Annual Recurring Revenue | MRR × 12 |
| **Churn** | Customer loss rate | Customers who cancel / total customers |
| **LTV** | Lifetime Value | Total revenue from a customer over time |
| **ARPU** | Average Revenue Per User | Total revenue / number of users |

---

## Billing Concepts

| Term | Definition | Notes |
|------|------------|-------|
| **Usage-based billing** | Charge based on consumption | API calls, storage, seats |
| **Subscription billing** | Fixed recurring charge | Monthly/annual plans |
| **Proration** | Partial charge for mid-cycle changes | Upgrade: charge difference for remaining days |
| **Metering** | Tracking usage for billing | Count events, aggregate |
| **Invoice** | Bill sent to customer | Line items, totals, due date |

---

## Discrepancy Types

| Type | What happens | Example |
|------|--------------|---------|
| **Missing usage** | Usage tracked but not billed | API calls logged but not invoiced |
| **Duplicate billing** | Same usage charged twice | Invoice generated twice |
| **Wrong pricing** | Incorrect rate applied | Old price used after upgrade |
| **Timing mismatch** | Wrong billing period | March usage on February invoice |
| **Unbilled overage** | Usage exceeds plan, not charged | 150 seats used, only 100 billed |

---

## Revenue Leakage

> Revenue leakage is money a business earned but didn't collect due to billing errors, system gaps, or process failures.

**Industry impact:** 1-5% of EBITDA lost annually (MGI Research)

**Common causes:**
- Manual data entry errors
- System integration gaps
- Outdated pricing isn’t updated
- Failed payment retries
- Contract terms aren’t enforced

---

## Reconciliation

| Term | Definition |
|------|------------|
| **Reconciliation** | Comparing two data sources to find discrepancies |
| **Usage reconciliation** | Compare usage logs vs billing records |
| **Payment reconciliation** | Compare invoices vs actual payments |

---

## Notes & Insights

(Add as learning progresses)

**Date:** ...
**Topic:** ...
**Insight:** ...
