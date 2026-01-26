package com.example.mini_saas.common.dto;

import java.time.Instant;

/**
 * Standard API error response.
 */
public record ApiErrorResponse(
        int status,
        String message,
        Instant timestamp
) {
    public ApiErrorResponse(int status, String message) {
        this(status, message, Instant.now());
    }

    public ApiErrorResponse(String message) {
        this(500, message, Instant.now());
    }
}