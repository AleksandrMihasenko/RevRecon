package com.revrecon.backend.service;

import com.revrecon.backend.dto.BillingRecordRequest;
import com.revrecon.backend.dto.BillingRecordResponse;
import com.revrecon.backend.model.BillingRecord;
import com.revrecon.backend.repository.BillingRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class BillingRecordService {
    private final BillingRecordRepository billingRecordRepository;

    public BillingRecordService(BillingRecordRepository billingRecordRepository) {
        this.billingRecordRepository = billingRecordRepository;
    }

    public BillingRecordResponse postBillingRecords(BillingRecordRequest billingRecordRequest) {
        BillingRecord billingRecord = new BillingRecord();
        billingRecord.setIdempotencyKey(billingRecordRequest.getIdempotencyKey());
        billingRecord.setCustomerId(billingRecordRequest.getCustomerId());
        billingRecord.setPeriodStart(billingRecordRequest.getPeriodStart());
        billingRecord.setPeriodEnd(billingRecordRequest.getPeriodEnd());
        billingRecord.setAmount(billingRecordRequest.getAmount());
        billingRecord.setStatus(billingRecordRequest.getStatus());

        BillingRecord savedBillingRecord = billingRecordRepository.insert(billingRecord);

        BillingRecordResponse response = new BillingRecordResponse();
        response.setId(savedBillingRecord.getId());
        response.setIdempotencyKey(savedBillingRecord.getIdempotencyKey());
        response.setCustomerId(savedBillingRecord.getCustomerId());
        response.setPeriodStart(savedBillingRecord.getPeriodStart());
        response.setPeriodEnd(savedBillingRecord.getPeriodEnd());
        response.setAmount(savedBillingRecord.getAmount());
        response.setStatus(savedBillingRecord.getStatus());
        response.setCreatedAt(savedBillingRecord.getCreatedAt());
        response.setUpdatedAt(savedBillingRecord.getUpdatedAt());
        return response;
    }
}


