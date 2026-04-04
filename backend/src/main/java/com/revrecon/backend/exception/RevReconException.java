package com.revrecon.backend.exception;

public class RevReconException extends RuntimeException {
    public RevReconException(String message) {
        super(message);
    }

    public RevReconException(String message, Throwable cause) {
        super(message, cause);
    }
}
