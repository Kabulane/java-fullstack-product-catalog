package com.example.productcatalog.service;

import com.example.productcatalog.dto.DashboardProductDto;
import com.example.productcatalog.dto.DashboardSummaryDto;
import com.example.productcatalog.repository.ProductRepository;
import com.example.productcatalog.repository.ReviewRepository;
import com.example.productcatalog.repository.projection.DashboardProductView;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private static final int DASHBOARD_PRODUCT_LIMIT = 5;
    private static final int RATING_SCALE = 2;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public DashboardService(
            ProductRepository productRepository,
            ReviewRepository reviewRepository
    ) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    public DashboardSummaryDto getSummary() {
        return new DashboardSummaryDto(
                productRepository.count(),
                reviewRepository.count()
        );
    }

    public List<DashboardProductDto> getMostAppreciatedProducts() {
        return productRepository.findMostAppreciatedProducts(topFive()).stream()
                .map(product -> toDto(product, product.getPositiveReviewCount()))
                .toList();
    }

    public List<DashboardProductDto> getLowestRatedProducts() {
        return productRepository.findLowestRatedProducts(topFive()).stream()
                .map(product -> toDto(product, null))
                .toList();
    }

    private DashboardProductDto toDto(
            DashboardProductView product,
            Long positiveReviewCount
    ) {
        return new DashboardProductDto(
                product.getReference(),
                product.getName(),
                product.getPrice(),
                product.getCurrency(),
                product.getReviewCount(),
                averageRating(product.getAverageRating()),
                positiveReviewCount
        );
    }

    private BigDecimal averageRating(Double averageRating) {
        if (averageRating == null) {
            return BigDecimal.ZERO.setScale(RATING_SCALE);
        }

        return BigDecimal.valueOf(averageRating)
                .setScale(RATING_SCALE, RoundingMode.HALF_UP);
    }

    private PageRequest topFive() {
        return PageRequest.of(0, DASHBOARD_PRODUCT_LIMIT);
    }
}
