package com.example.mini_saas.common.dto;

import com.example.mini_saas.tasks.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(
        @NotNull(message = "Status is required")
        TaskStatus status) {
}
