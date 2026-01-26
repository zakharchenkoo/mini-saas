package com.example.mini_saas.common.dto;

import com.example.mini_saas.tasks.TaskEntity;
import com.example.mini_saas.tasks.TaskStatus;

import java.time.Instant;

/**
 * DTO for task response.
 */
public record TaskResponseDto(
        Long id,
        String title,
        TaskStatus status,
        boolean completed,
        Long ownerId,
        Instant createdAt,
        Instant updatedAt
) {
    public static TaskResponseDto from(TaskEntity task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.isCompleted(),
                task.getOwner().getId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}