package com.revrecon.backend.model;

import java.math.BigDecimal;
import java.util.List;

public record ExpectedChargeResult(BigDecimal total, List<String> uncoveredMetrics) {

}
