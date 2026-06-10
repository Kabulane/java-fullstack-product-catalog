package com.example.productcatalog.repository;

import com.example.productcatalog.domain.entity.Product;
import com.example.productcatalog.repository.projection.ProductCatalogView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByReference(String reference);

    long count();

    @Query("""
            SELECT
                p.reference AS reference,
                p.name AS name,
                p.description AS description,
                p.price AS price,
                p.currency AS currency,
                p.stock AS stock,
                COUNT(r) AS reviewCount,
                AVG(r.notation) AS averageRating
            FROM Product p
            LEFT JOIN p.reviews r
            GROUP BY p.id, p.reference, p.name, p.description, p.price, p.currency, p.stock
            ORDER BY p.reference
            """)
    List<ProductCatalogView> findCatalog();

    @Query("""
            SELECT
                p.reference AS reference,
                p.name AS name,
                p.description AS description,
                p.price AS price,
                p.currency AS currency,
                p.stock AS stock,
                COUNT(r) AS reviewCount,
                AVG(r.notation) AS averageRating
            FROM Product p
            LEFT JOIN p.reviews r
            WHERE p.reference = :reference
            GROUP BY p.id, p.reference, p.name, p.description, p.price, p.currency, p.stock
            """)
    Optional<ProductCatalogView> findCatalogProductByReference(@Param("reference") String reference);
}
