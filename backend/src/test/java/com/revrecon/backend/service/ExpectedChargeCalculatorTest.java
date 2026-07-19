package com.revrecon.backend.service;

import com.revrecon.backend.model.ExpectedChargeResult;
import com.revrecon.backend.model.UsageMetricTotal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpectedChargeCalculatorTest {
    @Test
    void calculateExpectedCharge_shouldReturnExpectedAmountAndEmptyUncoveredMetrics_whenSingleMetricHasPerEventPrice() {
        // Arrange
        ExpectedChargeCalculator calculator = new ExpectedChargeCalculator();
        List<UsageMetricTotal> usageTotals = List.of(
                new UsageMetricTotal("api_calls", new BigDecimal("1200"))
        );
        String pricesJson = "{\"api_calls\": 0.01}";

        // Act
        ExpectedChargeResult result = calculator.calculate(usageTotals, pricesJson);

        // Assert
        assertEquals(0, new BigDecimal("12.00").compareTo(result.total()));
        assertTrue(result.uncoveredMetrics().isEmpty());
    }

    @Test
    void calculateExpectedCharge_shouldReturnTotalExpectedAmountAndEmptyUncoveredMetrics_whenMultipleMetricsHavePerEventPrices() {
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
        ExpectedChargeResult result = calculator.calculate(usageTotals, pricesJson);

        // Assert
        assertEquals(0, new BigDecimal("17.00").compareTo(result.total()));
        assertTrue(result.uncoveredMetrics().isEmpty());
    }

    @Test
    void calculateExpectedCharge_shouldReturnTotalExpectedAmountAndUncoveredMetrics_whenSomeMetricsHavePerEventPricesAndSomeNot() {
        // Arrange
        ExpectedChargeCalculator calculator = new ExpectedChargeCalculator();
        List<UsageMetricTotal> usageTotals = List.of(
                new UsageMetricTotal("api_calls", new BigDecimal("1200")),
                new UsageMetricTotal("storage_gb", new BigDecimal("10"))
        );
        String pricesJson = """
                    {
                        "api_calls": 0.01
                    }
                """;

        // Act
        ExpectedChargeResult result = calculator.calculate(usageTotals, pricesJson);

        // Assert
        assertEquals(0, new BigDecimal("12.00").compareTo(result.total()));
        assertEquals(1, result.uncoveredMetrics().size());
        assertEquals("storage_gb", result.uncoveredMetrics().get(0));
    }
}
