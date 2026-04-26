package com.revrecon.backend.exception;

import lombok.Getter;

@Getter
public class DuplicateEventException extends RevReconException {
    private final String idempotencyKey;

    public DuplicateEventException(String idempotencyKey) {
        super("Event with idempotency_key already exists: " + idempotencyKey);
        this.idempotencyKey = idempotencyKey;
    }
}
