package com.greenfoxacademy.foxshopnullpointerninjasotocyon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties(JwtConfigProperties.class)
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtConfigProperties {
    private Long expirationTimeMinutes;
    private String secret;
}
