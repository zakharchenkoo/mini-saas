package com.example.mini_saas.common.mapper;

import com.example.mini_saas.common.dto.UserResponseDto;
import com.example.mini_saas.users.UserEntity;

import java.util.Objects;

/**
 * Mapper for User entity.
 */
public final class UserMapper {

    private UserMapper() {
        // Utility class
    }

    public static UserResponseDto toDto(UserEntity entity) {
        Objects.requireNonNull(entity, "UserEntity cannot be null");

        return new UserResponseDto(
                entity.getId(),
                entity.getEmail(),
                entity.getRole(),
                entity.getCreatedAt()
        );
    }
}
