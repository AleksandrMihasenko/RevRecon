package com.revrecon.backend.controller;

import com.revrecon.backend.dto.DiscrepancyResponse;
import com.revrecon.backend.service.DiscrepancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DiscrepancyController {
    private final DiscrepancyService discrepancyService;

    @Autowired
    public DiscrepancyController(DiscrepancyService discrepancyService) {
        this.discrepancyService = discrepancyService;
    }

    @GetMapping("/discrepancies")
    public ResponseEntity<List<DiscrepancyResponse>> getDiscrepancies(@RequestParam(required = true) Long customerId, @RequestParam(required = true)Instant periodStart, @RequestParam(required = true)Instant periodEnd) {
        List<DiscrepancyResponse> response = discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
