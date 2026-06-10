package com.example.productcatalog.controller;

import com.example.productcatalog.domain.enums.AuthorType;
import com.example.productcatalog.dto.AuthorDto;
import com.example.productcatalog.dto.ProductDetailDto;
import com.example.productcatalog.dto.ReviewDto;
import com.example.productcatalog.service.CatalogService;
import com.example.productcatalog.service.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {

    @Mock
    private CatalogService catalogService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new CatalogController(catalogService)).build();
    }

    @Test
    void returnsProductDetails() throws Exception {
        when(catalogService.getProduct("59033201")).thenReturn(new ProductDetailDto(
                "59033201",
                "Test product",
                "Test description",
                new BigDecimal("12.50"),
                "EUR",
                4,
                2,
                new BigDecimal("4.50")
        ));

        mockMvc.perform(get("/api/products/59033201"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value("59033201"))
                .andExpect(jsonPath("$.name").value("Test product"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.price").value(12.50))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.stock").value(4))
                .andExpect(jsonPath("$.reviewCount").value(2))
                .andExpect(jsonPath("$.averageRating").value(4.50));
    }

    @Test
    void returnsProductReviewsWithAuthors() throws Exception {
        when(catalogService.getReviews("59033201")).thenReturn(List.of(
                new ReviewDto(
                        5,
                        LocalDateTime.of(2025, 4, 6, 2, 9, 10),
                        "Very practical and easy to use.",
                        new AuthorDto("Poha", "Kuxero", AuthorType.PROFESSIONAL)
                )
        ));

        mockMvc.perform(get("/api/products/59033201/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notation").value(5))
                .andExpect(jsonPath("$[0].reviewedAt").value("2025-04-06T02:09:10"))
                .andExpect(jsonPath("$[0].comment").value("Very practical and easy to use."))
                .andExpect(jsonPath("$[0].author.firstName").value("Poha"))
                .andExpect(jsonPath("$[0].author.lastName").value("Kuxero"))
                .andExpect(jsonPath("$[0].author.type").value("PROFESSIONAL"));
    }

    @Test
    void returnsNotFoundForReviewsOfUnknownProduct() throws Exception {
        when(catalogService.getReviews("unknown"))
                .thenThrow(new ProductNotFoundException("unknown"));

        mockMvc.perform(get("/api/products/unknown/reviews"))
                .andExpect(status().isNotFound())
                .andExpect(result -> org.hamcrest.MatcherAssert.assertThat(
                        result.getResolvedException(),
                        instanceOf(ProductNotFoundException.class)
                ));
    }
}
