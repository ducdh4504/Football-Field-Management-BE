package com.prm393.footballfieldmanagement.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank(message = "JWT_SECRET must be configured")
    private String secret;

    @Min(value = 1, message = "JWT access token expiration must be positive")
    private long accessTokenExpirationSeconds = 86_400;
}
