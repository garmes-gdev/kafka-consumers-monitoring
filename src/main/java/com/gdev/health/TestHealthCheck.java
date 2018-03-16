package com.gdev.health;

import com.codahale.metrics.health.HealthCheck;

public class TestHealthCheck extends HealthCheck {

    public TestHealthCheck() { }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
