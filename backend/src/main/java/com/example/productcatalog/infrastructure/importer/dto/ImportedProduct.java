package com.example.productcatalog.infrastructure.importer.dto;

import java.math.BigDecimal;
import java.util.List;

public record ImportedProduct(
        String reference,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        List<ImportedReview> reviews,
        String currency
) {
}
