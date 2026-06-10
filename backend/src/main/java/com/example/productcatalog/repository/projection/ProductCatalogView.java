package com.example.productcatalog.repository.projection;

import java.math.BigDecimal;

public interface ProductCatalogView {

    String getReference();

    String getName();

    String getDescription();

    BigDecimal getPrice();

    String getCurrency();

    Integer getStock();

    Long getReviewCount();

    Double getAverageRating();
}
