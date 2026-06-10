package com.example.productcatalog.dto;

import java.math.BigDecimal;

public record ProductSummaryDto(
        String reference,
        String name,
        BigDecimal price,
        String currency,
        Integer stock,
        long reviewCount,
        BigDecimal averageRating
) {
}
