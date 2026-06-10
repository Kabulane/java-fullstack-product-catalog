package com.example.productcatalog.dto;

import java.math.BigDecimal;

public record ProductDetailDto(
        String reference,
        String name,
        String description,
        BigDecimal price,
        String currency,
        Integer stock,
        long reviewCount,
        BigDecimal averageRating
) {
}
