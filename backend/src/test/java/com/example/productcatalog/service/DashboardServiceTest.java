package com.example.productcatalog.service;

import com.example.productcatalog.dto.DashboardProductDto;
import com.example.productcatalog.dto.DashboardSummaryDto;
import com.example.productcatalog.repository.ProductRepository;
import com.example.productcatalog.repository.ReviewRepository;
import com.example.productcatalog.repository.projection.DashboardProductView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private DashboardProductView productView;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void returnsDashboardSummary() {
        when(productRepository.count()).thenReturn(250L);
        when(reviewRepository.count()).thenReturn(766L);

        DashboardSummaryDto summary = dashboardService.getSummary();

        assertThat(summary.totalProducts()).isEqualTo(250);
        assertThat(summary.totalReviews()).isEqualTo(766);
    }

    @Test
    void returnsMostAppreciatedProducts() {
        givenDashboardProduct(12L, 4.666666);
        when(productView.getPositiveReviewCount()).thenReturn(10L);
        when(productRepository.findMostAppreciatedProducts(PageRequest.of(0, 5)))
                .thenReturn(List.of(productView));

        List<DashboardProductDto> products = dashboardService.getMostAppreciatedProducts();

        assertThat(products).singleElement().satisfies(product -> {
            assertThat(product.reference()).isEqualTo("59033201");
            assertThat(product.reviewCount()).isEqualTo(12);
            assertThat(product.averageRating()).isEqualByComparingTo("4.67");
            assertThat(product.positiveReviewCount()).isEqualTo(10);
        });
    }

    @Test
    void returnsLowestRatedProductsWithoutPositiveReviewCount() {
        givenDashboardProduct(6L, 1.833333);
        when(productRepository.findLowestRatedProducts(PageRequest.of(0, 5)))
                .thenReturn(List.of(productView));

        List<DashboardProductDto> products = dashboardService.getLowestRatedProducts();

        assertThat(products).singleElement().satisfies(product -> {
            assertThat(product.reference()).isEqualTo("59033201");
            assertThat(product.reviewCount()).isEqualTo(6);
            assertThat(product.averageRating()).isEqualByComparingTo("1.83");
            assertThat(product.positiveReviewCount()).isNull();
        });
    }

    private void givenDashboardProduct(
            long reviewCount,
            double averageRating
    ) {
        when(productView.getReference()).thenReturn("59033201");
        when(productView.getName()).thenReturn("Test product");
        when(productView.getPrice()).thenReturn(new BigDecimal("12.50"));
        when(productView.getCurrency()).thenReturn("EUR");
        when(productView.getReviewCount()).thenReturn(reviewCount);
        when(productView.getAverageRating()).thenReturn(averageRating);
    }
}
