package com.demo.upimesh.model;

public record HealthStatus(
    String status,
    int activeBridges,
    boolean rateLimiterActive
) {}
