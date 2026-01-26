package com.example.mini_saas.auth;

import com.example.mini_saas.common.dto.UserResponseDto;
import com.example.mini_saas.common.mapper.UserMapper;
import com.example.mini_saas.users.UserEntity;
import com.example.mini_saas.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {
        String email = extractEmail(authentication);
        UserEntity user = userService.findByEmail(email);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    private String extractEmail(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("email");
        }
        return authentication.getName();
    }
}