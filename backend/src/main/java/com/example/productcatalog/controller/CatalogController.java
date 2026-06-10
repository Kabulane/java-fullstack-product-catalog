package com.example.productcatalog.controller;

import com.example.productcatalog.dto.ProductDetailDto;
import com.example.productcatalog.dto.ProductSummaryDto;
import com.example.productcatalog.dto.ReviewDto;
import com.example.productcatalog.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<ProductSummaryDto> getProducts() {
        return catalogService.getProducts();
    }

    @GetMapping("/{reference}")
    public ProductDetailDto getProduct(@PathVariable String reference) {
        return catalogService.getProduct(reference);
    }

    @GetMapping("/{reference}/reviews")
    public List<ReviewDto> getReviews(@PathVariable String reference) {
        return catalogService.getReviews(reference);
    }
}
