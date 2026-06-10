package com.example.productcatalog.dto;

import java.time.LocalDateTime;

public record ReviewDto(
        Integer notation,
        LocalDateTime reviewedAt,
        String comment,
        AuthorDto author
) {
}
