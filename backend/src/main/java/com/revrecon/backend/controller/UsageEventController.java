package com.revrecon.backend.controller;

import com.revrecon.backend.dto.UsageEventRequest;
import com.revrecon.backend.dto.UsageEventResponse;
import com.revrecon.backend.exception.DuplicateEventException;
import com.revrecon.backend.service.UsageEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UsageEventController {
    private final UsageEventService usageEventService;

    @Autowired
    public UsageEventController(UsageEventService usageEventService) {
        this.usageEventService = usageEventService;
    }

    @PostMapping("/usage-events")
    public ResponseEntity<?> register(@Valid @RequestBody UsageEventRequest request) {
        try {
            UsageEventResponse response = usageEventService.postEvents(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicateEventException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
