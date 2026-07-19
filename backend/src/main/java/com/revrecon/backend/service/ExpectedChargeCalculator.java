package com.revrecon.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.revrecon.backend.model.ExpectedChargeResult;
import com.revrecon.backend.model.UsageMetricTotal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpectedChargeCalculator {
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public ExpectedChargeResult calculate(List<UsageMetricTotal> usageTotals, String pricesJson) {
        Map<String, BigDecimal> prices = parsePrices(pricesJson);

        BigDecimal total = BigDecimal.ZERO;
        List<String> uncoveredMetrics = new ArrayList<>();

        for (UsageMetricTotal usage : usageTotals) {
            if (!prices.containsKey(usage.getMetric())) {
                uncoveredMetrics.add(usage.getMetric());
                continue;
            }

            BigDecimal price = prices.get(usage.getMetric());
            BigDecimal charge = usage.getTotalQuantity().multiply(price);
            total = total.add(charge);
        }

        return new ExpectedChargeResult(total, uncoveredMetrics);
    }

    private Map<String, BigDecimal> parsePrices(String pricesJson) {
        try {
            return jsonMapper.readValue(
                    pricesJson,
                    new TypeReference<Map<String, BigDecimal>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON format for prices", e);
        }
    }
}
