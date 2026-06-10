package com.example.productcatalog.infrastructure.importer.dto;

import java.time.LocalDateTime;

public record ImportedReview(
        Integer notation,
        LocalDateTime date,
        String comment,
        ImportedAuthor author
) {
}
