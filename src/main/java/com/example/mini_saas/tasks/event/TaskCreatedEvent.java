package com.example.mini_saas.tasks.event;

import java.time.Instant;

/**
 * Domain event published when a task is created.
 * Contains only data, not entities.
 */
public record TaskCreatedEvent(
        Long taskId,
        String title,
        Long ownerId,
        Instant createdAt
) {}