package com.example.productcatalog.infrastructure.importer;

import com.example.productcatalog.domain.entity.Product;
import com.example.productcatalog.repository.AuthorRepository;
import com.example.productcatalog.repository.ProductRepository;
import com.example.productcatalog.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(properties = "catalog.import.enabled=false")
class ProductCatalogImporterTest {

    @Autowired
    private ProductCatalogImporter importer;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void clearDatabase() {
        reviewRepository.deleteAll();
        productRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void importsDatasetIntoEmptyDatabase() {
        importer.importIfEmpty();

        assertThat(productRepository.count()).isEqualTo(250);
        assertThat(authorRepository.count()).isEqualTo(766);
        assertThat(reviewRepository.count()).isEqualTo(766);
        assertThat(productRepository.existsByReference("59033201")).isTrue();
    }

    @Test
    void skipsImportWhenProductsAlreadyExist() {
        productRepository.save(new Product(
                "existing",
                "Existing product",
                "Existing product used to verify the import guard.",
                BigDecimal.ONE,
                1,
                "EUR"
        ));

        importer.importIfEmpty();

        assertThat(productRepository.count()).isEqualTo(1);
        assertThat(authorRepository.count()).isZero();
        assertThat(reviewRepository.count()).isZero();
    }
}
