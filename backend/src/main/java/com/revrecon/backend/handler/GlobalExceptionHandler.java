package com.revrecon.backend.handler;

import com.revrecon.backend.dto.UsageEventErrorResponse;
import com.revrecon.backend.exception.DuplicateEventException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateEventException.class)
    public ResponseEntity<UsageEventErrorResponse> handleDuplicateEventException(DuplicateEventException e) {
        UsageEventErrorResponse error = new UsageEventErrorResponse();
        error.setCode("DUPLICATE_EVENT");
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
