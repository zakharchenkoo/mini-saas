package com.example.mini_saas.common.exception;

/**
 * Thrown when an entity cannot be found.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
