package com.prm393.footballfieldmanagement.config;

import com.prm393.footballfieldmanagement.entity.User;
import com.prm393.footballfieldmanagement.enums.Role;
import com.prm393.footballfieldmanagement.service.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtConfigTest {

    @Test
    void hs256EncoderAndDecoderUseTheConfiguredSecret() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("local-test-secret-must-be-at-least-32-bytes");
        JwtConfig config = new JwtConfig();
        SecretKey secretKey = config.jwtSecretKey(properties);
        JwtTokenService tokenService = new JwtTokenService(config.jwtEncoder(secretKey), properties);

        String token = tokenService.generateAccessToken(User.builder()
                .userId(7L)
                .email("customer@test.com")
                .role(Role.CUSTOMER)
                .build());
        Jwt decoded = config.jwtDecoder(secretKey).decode(token);

        assertEquals("7", decoded.getSubject());
        assertEquals("customer@test.com", decoded.getClaimAsString("email"));
        assertEquals("CUSTOMER", decoded.getClaimAsString("role"));
    }
}
