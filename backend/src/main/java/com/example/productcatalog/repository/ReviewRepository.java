package com.example.productcatalog.repository;

import com.example.productcatalog.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
            SELECT r
            FROM Review r
            JOIN FETCH r.author
            WHERE r.product.reference = :reference
            ORDER BY r.reviewedAt DESC
            """)
    List<Review> findByProductReferenceWithAuthor(@Param("reference") String reference);
}
