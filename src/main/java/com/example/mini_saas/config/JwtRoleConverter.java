package com.example.mini_saas.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converts Keycloak realm roles to Spring Security authorities.
 */
public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Logger log = LoggerFactory.getLogger(JwtRoleConverter.class);

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        try {
            // Extract realm_access claim
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            if (realmAccess == null) {
                log.debug("No realm_access claim found in JWT");
                return Collections.emptyList();
            }

            // Extract roles
            List<String> roles = (List<String>) realmAccess.get("roles");

            if (roles == null || roles.isEmpty()) {
                log.debug("No roles found in realm_access");
                return Collections.emptyList();
            }

            // Convert to authorities
            List<GrantedAuthority> authorities = roles.stream()
                    .filter(role -> role != null && !role.isBlank())
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());

            log.debug("Converted {} roles to authorities", authorities.size());
            return authorities;

        } catch (Exception e) {
            log.error("Error converting JWT roles", e);
            return Collections.emptyList();
        }
    }
}