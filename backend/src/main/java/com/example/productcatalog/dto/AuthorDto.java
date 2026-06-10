package com.example.productcatalog.dto;

import com.example.productcatalog.domain.enums.AuthorType;

public record AuthorDto(
        String firstName,
        String lastName,
        AuthorType type
) {
}
