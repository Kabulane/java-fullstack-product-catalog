package com.example.productcatalog.service;

import com.example.productcatalog.domain.entity.Author;
import com.example.productcatalog.domain.entity.Product;
import com.example.productcatalog.domain.entity.Review;
import com.example.productcatalog.domain.enums.AuthorType;
import com.example.productcatalog.dto.ProductDetailDto;
import com.example.productcatalog.dto.ProductSummaryDto;
import com.example.productcatalog.dto.ReviewDto;
import com.example.productcatalog.repository.ProductRepository;
import com.example.productcatalog.repository.ReviewRepository;
import com.example.productcatalog.repository.projection.ProductCatalogView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductCatalogView productView;

    @InjectMocks
    private CatalogService catalogService;

    @Test
    void returnsProductSummariesWithRoundedAverageRating() {
        givenProductView(3L, 3.666666);
        when(productRepository.findCatalog()).thenReturn(List.of(productView));

        List<ProductSummaryDto> products = catalogService.getProducts();

        assertThat(products).singleElement().satisfies(product -> {
            assertThat(product.reference()).isEqualTo("59033201");
            assertThat(product.name()).isEqualTo("Test product");
            assertThat(product.price()).isEqualByComparingTo("12.50");
            assertThat(product.currency()).isEqualTo("EUR");
            assertThat(product.stock()).isEqualTo(4);
            assertThat(product.reviewCount()).isEqualTo(3);
            assertThat(product.averageRating()).isEqualByComparingTo("3.67");
        });
    }

    @Test
    void returnsZeroAverageRatingForProductWithoutReviews() {
        givenProductView(0L, null);
        when(productRepository.findCatalog()).thenReturn(List.of(productView));

        ProductSummaryDto product = catalogService.getProducts().getFirst();

        assertThat(product.reviewCount()).isZero();
        assertThat(product.averageRating()).isEqualByComparingTo("0.00");
    }

    @Test
    void returnsProductDetails() {
        givenProductView(2L, 4.5);
        when(productView.getDescription()).thenReturn("Test description");
        when(productRepository.findCatalogProductByReference("59033201"))
                .thenReturn(Optional.of(productView));

        ProductDetailDto product = catalogService.getProduct("59033201");

        assertThat(product.reference()).isEqualTo("59033201");
        assertThat(product.description()).isEqualTo("Test description");
        assertThat(product.reviewCount()).isEqualTo(2);
        assertThat(product.averageRating()).isEqualByComparingTo("4.50");
    }

    @Test
    void throwsWhenProductDetailsAreUnknown() {
        when(productRepository.findCatalogProductByReference("unknown"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> catalogService.getProduct("unknown"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found: unknown");
    }

    @Test
    void returnsReviewsWithAuthorDetails() {
        Author author = new Author("Poha", "Kuxero", AuthorType.PROFESSIONAL);
        Product product = new Product(
                "59033201",
                "Test product",
                "Test description",
                new BigDecimal("12.50"),
                4,
                "EUR"
        );
        LocalDateTime reviewedAt = LocalDateTime.of(2025, 4, 6, 2, 9, 10);
        Review review = new Review(
                product,
                author,
                5,
                reviewedAt,
                "Very practical and easy to use."
        );

        when(productRepository.existsByReference("59033201")).thenReturn(true);
        when(reviewRepository.findByProductReferenceWithAuthor("59033201"))
                .thenReturn(List.of(review));

        List<ReviewDto> reviews = catalogService.getReviews("59033201");

        assertThat(reviews).singleElement().satisfies(result -> {
            assertThat(result.notation()).isEqualTo(5);
            assertThat(result.reviewedAt()).isEqualTo(reviewedAt);
            assertThat(result.comment()).isEqualTo("Very practical and easy to use.");
            assertThat(result.author().firstName()).isEqualTo("Poha");
            assertThat(result.author().lastName()).isEqualTo("Kuxero");
            assertThat(result.author().type()).isEqualTo(AuthorType.PROFESSIONAL);
        });
    }

    @Test
    void throwsBeforeLoadingReviewsWhenProductIsUnknown() {
        when(productRepository.existsByReference("unknown")).thenReturn(false);

        assertThatThrownBy(() -> catalogService.getReviews("unknown"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found: unknown");

        verify(reviewRepository, never()).findByProductReferenceWithAuthor("unknown");
    }

    private void givenProductView(long reviewCount, Double averageRating) {
        when(productView.getReference()).thenReturn("59033201");
        when(productView.getName()).thenReturn("Test product");
        when(productView.getPrice()).thenReturn(new BigDecimal("12.50"));
        when(productView.getCurrency()).thenReturn("EUR");
        when(productView.getStock()).thenReturn(4);
        when(productView.getReviewCount()).thenReturn(reviewCount);
        when(productView.getAverageRating()).thenReturn(averageRating);
    }
}
