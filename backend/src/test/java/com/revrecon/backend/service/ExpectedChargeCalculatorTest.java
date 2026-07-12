package com.revrecon.backend.service;

import com.revrecon.backend.model.UsageMetricTotal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpectedChargeCalculatorTest {
    @Test
    void calculateExpectedCharge_shouldReturnExpectedAmount_whenSingleMetricHasPerEventPrice() {
        // Arrange
        ExpectedChargeCalculator calculator = new ExpectedChargeCalculator();
        List<UsageMetricTotal> usageTotals = List.of(
                new UsageMetricTotal("api_calls", new BigDecimal("1200"))
        );
        String pricesJson = "{\"api_calls\": 0.01}";

        // Act
        BigDecimal expectedAmount = calculator.calculate(usageTotals, pricesJson);

        // Assert
        assertEquals(0, new BigDecimal("12.00").compareTo(expectedAmount));
    }

    @Test
    void calculateExpectedCharge_shouldReturnTotalExpectedAmount_whenMultipleMetricsHavePerEventPrices() {
        // Arrange
        ExpectedChargeCalculator calculator = new ExpectedChargeCalculator();
        List<UsageMetricTotal> usageTotals = List.of(
                new UsageMetricTotal("api_calls", new BigDecimal("1200")),
                new UsageMetricTotal("storage_gb", new BigDecimal("10"))
        );
        String pricesJson = """
                    {
                        "api_calls": 0.01,
                        "storage_gb": 0.50
                    }
                """;

        // Act
        BigDecimal expectedAmount = calculator.calculate(usageTotals, pricesJson);

        // Assert
        assertEquals(0, new BigDecimal("17.00").compareTo(expectedAmount));
    }
}
