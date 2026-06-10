package com.example.productcatalog.dto;

import java.math.BigDecimal;

public record DashboardProductDto(
        String reference,
        String name,
        BigDecimal price,
        String currency,
        long reviewCount,
        BigDecimal averageRating,
        Long positiveReviewCount
) {
}
