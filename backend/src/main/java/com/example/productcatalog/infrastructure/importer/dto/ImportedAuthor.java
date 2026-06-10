package com.example.productcatalog.infrastructure.importer.dto;

public record ImportedAuthor(
        String firstName,
        String lastName,
        String type
) {
}
