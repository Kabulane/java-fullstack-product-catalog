package com.example.productcatalog.controller;

import com.example.productcatalog.dto.DashboardProductDto;
import com.example.productcatalog.dto.DashboardSummaryDto;
import com.example.productcatalog.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummaryDto getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/most-appreciated-products")
    public List<DashboardProductDto> getMostAppreciatedProducts() {
        return dashboardService.getMostAppreciatedProducts();
    }

    @GetMapping("/lowest-rated-products")
    public List<DashboardProductDto> getLowestRatedProducts() {
        return dashboardService.getLowestRatedProducts();
    }
}
