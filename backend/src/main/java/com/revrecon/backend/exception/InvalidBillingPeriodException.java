package com.revrecon.backend.exception;

public class InvalidBillingPeriodException extends RevReconException {
    public InvalidBillingPeriodException() {
        super("periodStart must not be after periodEnd");
    }
}
