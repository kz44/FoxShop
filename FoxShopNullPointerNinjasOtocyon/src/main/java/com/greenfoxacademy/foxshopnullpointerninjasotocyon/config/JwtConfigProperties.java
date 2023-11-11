package com.greenfoxacademy.foxshopnullpointerninjasotocyon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


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
