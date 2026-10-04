package com.prm393.footballfieldmanagement.service;

import com.prm393.footballfieldmanagement.config.JwtProperties;
import com.prm393.footballfieldmanagement.entity.User;
import com.prm393.footballfieldmanagement.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Test
    void accessTokenContainsOnlyRequiredIdentityClaimsAndExpiresAfterTwentyFourHours() {
        JwtProperties properties = new JwtProperties();
        properties.setAccessTokenExpirationSeconds(86_400);
        JwtTokenService tokenService = new JwtTokenService(jwtEncoder, properties);
        Instant now = Instant.now();
        when(jwtEncoder.encode(any())).thenReturn(Jwt.withTokenValue("signed-token")
                .header("alg", "HS256")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(86_400))
                .build());

        String token = tokenService.generateAccessToken(User.builder()
                .userId(7L)
                .email("customer@test.com")
                .role(Role.CUSTOMER)
                .build());

        ArgumentCaptor<JwtEncoderParameters> parametersCaptor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder).encode(parametersCaptor.capture());
        JwtEncoderParameters parameters = parametersCaptor.getValue();
        assertEquals("7", parameters.getClaims().getSubject());
        assertEquals("customer@test.com", parameters.getClaims().getClaimAsString("email"));
        assertEquals("CUSTOMER", parameters.getClaims().getClaimAsString("role"));
        assertEquals(86_400, Duration.between(
                parameters.getClaims().getIssuedAt(), parameters.getClaims().getExpiresAt()).getSeconds());
        assertNotNull(parameters.getJwsHeader());
        assertEquals("signed-token", token);
        assertEquals(86_400, tokenService.getAccessTokenExpirationSeconds());
    }
}
