# RevRecon

Detect where usage and billing don’t match and explain why.

## What is this

RevRecon is a small backend project exploring how billing systems fail in practice.

It focuses on a simple question:

Why does usage not match what gets billed?

## What it does

- ingests usage events and billing records
- compares expected vs actual billing
- detects mismatches (missing, duplicated, wrong price, timing issues)
- explains why they happen

## Why

In many systems:
- usage is tracked separately from billing
- pricing rules are complex
- data is often inconsistent

As a result, companies:
- underbill
- overbill
- fix things manually

This project explores how to detect and understand these problems.

## Scope

Current focus:
- usage → billing reconciliation
- discrepancy detection
- basic explainability

Planned:
- alerts
- simulation (what-if pricing / usage scenarios)

## Status

Work in progress.
