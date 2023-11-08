package com.greenfoxacademy.foxshopnullpointerninjasotocyon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@EnableConfigurationProperties(JwtConfigProperties.class)
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfigProperties {

    private Long expirationTimeMinutes;

    private String secret;
}
