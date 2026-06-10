package com.example.productcatalog.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HealthControllerTest {

    private final HealthController controller = new HealthController();

    @Test
    void returnsOk() {
        assertThat(controller.health()).isEqualTo("OK");
    }
}
