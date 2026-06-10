package com.example.productcatalog.infrastructure.importer;

import com.example.productcatalog.domain.entity.Author;
import com.example.productcatalog.domain.entity.Product;
import com.example.productcatalog.domain.entity.Review;
import com.example.productcatalog.domain.enums.AuthorType;
import com.example.productcatalog.infrastructure.importer.dto.ImportedAuthor;
import com.example.productcatalog.infrastructure.importer.dto.ImportedProduct;
import com.example.productcatalog.infrastructure.importer.dto.ImportedReview;
import com.example.productcatalog.repository.AuthorRepository;
import com.example.productcatalog.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ProductCatalogImporter implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCatalogImporter.class);
    private static final TypeReference<List<ImportedProduct>> PRODUCT_LIST_TYPE = new TypeReference<>() {
    };

    private final ProductRepository productRepository;
    private final AuthorRepository authorRepository;
    private final ObjectMapper objectMapper;
    private final Path importFile;
    private final boolean importEnabled;

    public ProductCatalogImporter(
            ProductRepository productRepository,
            AuthorRepository authorRepository,
            ObjectMapper objectMapper,
            @Value("${catalog.import.file:../data/products.json}") String importFile,
            @Value("${catalog.import.enabled:true}") boolean importEnabled
    ) {
        this.productRepository = productRepository;
        this.authorRepository = authorRepository;
        this.objectMapper = objectMapper;
        this.importFile = Path.of(importFile);
        this.importEnabled = importEnabled;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (importEnabled) {
            importIfEmpty();
        }
    }

    @Transactional
    public void importIfEmpty() {
        if (productRepository.count() > 0) {
            LOGGER.info("Product catalog already contains data; JSON import skipped");
            return;
        }

        List<ImportedProduct> importedProducts = readProducts();
        List<Product> products = new ArrayList<>(importedProducts.size());
        List<Author> authors = new ArrayList<>();
        int reviewCount = 0;

        for (ImportedProduct importedProduct : importedProducts) {
            Product product = toProduct(importedProduct);

            for (ImportedReview importedReview : importedProduct.reviews()) {
                Author author = toAuthor(importedReview.author());
                authors.add(author);
                new Review(
                        product,
                        author,
                        importedReview.notation(),
                        importedReview.date(),
                        importedReview.comment()
                );
                reviewCount++;
            }

            products.add(product);
        }

        authorRepository.saveAll(authors);
        productRepository.saveAll(products);

        LOGGER.info("Imported {} products and {} reviews", products.size(), reviewCount);
    }

    private List<ImportedProduct> readProducts() {
        Path resolvedImportFile = importFile.toAbsolutePath().normalize();

        try (InputStream inputStream = Files.newInputStream(resolvedImportFile)) {
            return objectMapper.readValue(inputStream, PRODUCT_LIST_TYPE);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not import product catalog from " + resolvedImportFile,
                    exception
            );
        }
    }

    private Product toProduct(ImportedProduct importedProduct) {
        return new Product(
                importedProduct.reference(),
                importedProduct.name(),
                importedProduct.description(),
                importedProduct.price(),
                importedProduct.stock(),
                importedProduct.currency()
        );
    }

    private Author toAuthor(ImportedAuthor importedAuthor) {
        AuthorType type = AuthorType.valueOf(importedAuthor.type().toUpperCase(Locale.ROOT));
        return new Author(importedAuthor.firstName(), importedAuthor.lastName(), type);
    }
}
