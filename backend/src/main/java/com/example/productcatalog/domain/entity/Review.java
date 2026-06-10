package com.example.productcatalog.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(nullable = false)
    private Integer notation;

    @Column(name = "reviewed_at", nullable = false)
    private LocalDateTime reviewedAt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    protected Review() {
    }

    public Review(
            Product product,
            Author author,
            Integer notation,
            LocalDateTime reviewedAt,
            String comment
    ) {
        this.author = author;
        this.notation = notation;
        this.reviewedAt = reviewedAt;
        this.comment = comment;
        product.addReview(this);
    }

    void assignTo(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Author getAuthor() {
        return author;
    }

    public Integer getNotation() {
        return notation;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public String getComment() {
        return comment;
    }
}
