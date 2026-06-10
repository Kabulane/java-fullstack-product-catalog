package com.example.productcatalog.repository;

import com.example.productcatalog.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByReference(String reference);

    long count();
}
