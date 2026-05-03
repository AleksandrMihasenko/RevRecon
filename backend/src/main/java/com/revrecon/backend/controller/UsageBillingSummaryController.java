package com.revrecon.backend.controller;

import com.revrecon.backend.dto.UsageBillingSummaryResponse;
import com.revrecon.backend.service.UsageBillingSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class UsageBillingSummaryController {
    private final UsageBillingSummaryService usageBillingSummaryService;

    @Autowired
    public UsageBillingSummaryController(UsageBillingSummaryService usageBillingSummaryService) {
        this.usageBillingSummaryService = usageBillingSummaryService;
    }

    @GetMapping("/usage-billing-summary")
    public ResponseEntity<UsageBillingSummaryResponse> getUsageBillingSummary(@RequestParam(required = true) Long customerId, @RequestParam(required = true)Instant periodStart, @RequestParam(required = true)Instant periodEnd) {
        UsageBillingSummaryResponse response = usageBillingSummaryService.getUsageBillingSummary(customerId, periodStart, periodEnd);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
