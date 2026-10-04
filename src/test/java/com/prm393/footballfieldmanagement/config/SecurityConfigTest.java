package com.prm393.footballfieldmanagement.config;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void adminRoleMapsToRoleAdminAuthority() {
        AbstractAuthenticationToken authentication = converter().convert(jwtWithRole("ADMIN"));

        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void customerRoleMapsToRoleCustomerAuthority() {
        AbstractAuthenticationToken authentication = converter().convert(jwtWithRole("CUSTOMER"));

        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER")));
    }

    @SuppressWarnings("unchecked")
    private Converter<Jwt, AbstractAuthenticationToken> converter() {
        return (Converter<Jwt, AbstractAuthenticationToken>) securityConfig.jwtAuthenticationConverter();
    }

    private Jwt jwtWithRole(String role) {
        Instant now = Instant.now();
        return Jwt.withTokenValue("access-token")
                .header("alg", "HS256")
                .subject("7")
                .claim("role", role)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(60))
                .build();
    }
}
