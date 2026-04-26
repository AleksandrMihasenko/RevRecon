package com.revrecon.backend.controller;

import com.revrecon.backend.dto.BillingRecordRequest;
import com.revrecon.backend.dto.BillingRecordResponse;
import com.revrecon.backend.service.BillingRecordService;
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
public class BillingController {
    private final BillingRecordService billingRecordService;

    @Autowired
    public BillingController(BillingRecordService billingRecordService) {
        this.billingRecordService = billingRecordService;
    }

    @PostMapping("/billing")
    public ResponseEntity<BillingRecordResponse> register(@Valid @RequestBody BillingRecordRequest request) {
        BillingRecordResponse response = billingRecordService.postBillingRecords(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
