package com.revrecon.backend.exception;

public class DuplicateEventException extends RevReconException {
    private final String idempotencyKey;

    public DuplicateEventException(String idempotencyKey) {
        super("Event with idempotency_key already exists: " + idempotencyKey);
        this.idempotencyKey = idempotencyKey;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }
}
