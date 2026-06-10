package com.example.productcatalog.repository.projection;

import java.math.BigDecimal;

public interface DashboardProductView {

    String getReference();

    String getName();

    BigDecimal getPrice();

    String getCurrency();

    Long getReviewCount();

    Double getAverageRating();

    Long getPositiveReviewCount();
}
