package com.revrecon.backend.service;

import com.revrecon.backend.dto.UsageEventRequest;
import com.revrecon.backend.dto.UsageEventResponse;
import com.revrecon.backend.model.UsageEvent;
import com.revrecon.backend.repository.UsageEventRepository;
import org.springframework.stereotype.Service;

@Service
public class UsageEventService {
    private final UsageEventRepository usageEventRepository;

    public UsageEventService(UsageEventRepository usageEventRepository) {
        this.usageEventRepository = usageEventRepository;
    }

    public UsageEventResponse postEvents(UsageEventRequest usageEventRequest) {
        UsageEvent usageEvent = new UsageEvent();
        usageEvent.setIdempotencyKey(usageEventRequest.getIdempotencyKey());
        usageEvent.setCustomerId(usageEventRequest.getCustomerId());
        usageEvent.setMetric(usageEventRequest.getMetric());
        usageEvent.setQuantity(usageEventRequest.getQuantity());
        usageEvent.setTimestamp(usageEventRequest.getTimestamp());

        UsageEvent savedUsageEvent = usageEventRepository.insert(usageEvent);

        UsageEventResponse response = new UsageEventResponse();
        response.setId(savedUsageEvent.getId());
        response.setIdempotencyKey(savedUsageEvent.getIdempotencyKey());
        response.setCustomerId(savedUsageEvent.getCustomerId());
        response.setMetric(savedUsageEvent.getMetric());
        response.setQuantity(savedUsageEvent.getQuantity());
        response.setTimestamp(savedUsageEvent.getTimestamp());
        response.setCreatedAt(savedUsageEvent.getCreatedAt());
        response.setUpdatedAt(savedUsageEvent.getUpdatedAt());
        return response;
    }
}


