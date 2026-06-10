package com.example.productcatalog.service;

import com.example.productcatalog.domain.entity.Author;
import com.example.productcatalog.domain.entity.Review;
import com.example.productcatalog.dto.AuthorDto;
import com.example.productcatalog.dto.ProductDetailDto;
import com.example.productcatalog.dto.ProductSummaryDto;
import com.example.productcatalog.dto.ReviewDto;
import com.example.productcatalog.repository.ProductRepository;
import com.example.productcatalog.repository.ReviewRepository;
import com.example.productcatalog.repository.projection.ProductCatalogView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CatalogService {

    private static final int RATING_SCALE = 2;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public CatalogService(
            ProductRepository productRepository,
            ReviewRepository reviewRepository
    ) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ProductSummaryDto> getProducts() {
        return productRepository.findCatalog().stream()
                .map(this::toSummary)
                .toList();
    }

    public ProductDetailDto getProduct(String reference) {
        ProductCatalogView product = productRepository.findCatalogProductByReference(reference)
                .orElseThrow(() -> new ProductNotFoundException(reference));

        return new ProductDetailDto(
                product.getReference(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCurrency(),
                product.getStock(),
                product.getReviewCount(),
                averageRating(product.getAverageRating())
        );
    }

    public List<ReviewDto> getReviews(String reference) {
        if (!productRepository.existsByReference(reference)) {
            throw new ProductNotFoundException(reference);
        }

        return reviewRepository.findByProductReferenceWithAuthor(reference).stream()
                .map(this::toReview)
                .toList();
    }

    private ProductSummaryDto toSummary(ProductCatalogView product) {
        return new ProductSummaryDto(
                product.getReference(),
                product.getName(),
                product.getPrice(),
                product.getCurrency(),
                product.getStock(),
                product.getReviewCount(),
                averageRating(product.getAverageRating())
        );
    }

    private ReviewDto toReview(Review review) {
        Author author = review.getAuthor();
        AuthorDto authorDto = new AuthorDto(
                author.getFirstName(),
                author.getLastName(),
                author.getType()
        );

        return new ReviewDto(
                review.getNotation(),
                review.getReviewedAt(),
                review.getComment(),
                authorDto
        );
    }

    private BigDecimal averageRating(Double averageRating) {
        if (averageRating == null) {
            return BigDecimal.ZERO.setScale(RATING_SCALE);
        }

        return BigDecimal.valueOf(averageRating)
                .setScale(RATING_SCALE, RoundingMode.HALF_UP);
    }
}
