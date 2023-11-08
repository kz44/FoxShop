package com.greenfoxacademy.foxshopnullpointerninjasotocyon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(JwtConfigProperties.class)
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtConfigProperties {

    /**
     * JwtConfigProperties contains configuration properties related to JSON webtoken
     *
     * expirationTimeMinutes: Specify the duration of JWT token in minutes, default value 60 minutes
     * secret: used for signing JWT token
     */
    private Long expirationTimeMinutes;
    private String secret;
}
