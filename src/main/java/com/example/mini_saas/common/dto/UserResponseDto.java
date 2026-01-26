package com.example.mini_saas.common.dto;

import com.example.mini_saas.users.Role;
import java.time.Instant;

/**
 * DTO representing user data exposed via API.
 */
public record UserResponseDto(
        Long id,
        String email,
        Role role,
        Instant createdAt
) {}