package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.config.JwtConfigProperties;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtConfigProperties jwtConfigProperties;

    /**
     * Generates a JWT token based on UserDetails
     *
     * @param userDetails containing user information
     * @return String of generated JWT token
     */
    public String generateToken(final UserDetails userDetails) {

        final var now = Instant.now();

        // Stores the information for token playload
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("firstName", ((User) userDetails).getFirstName());
        claims.put("lastName", ((User) userDetails).getLastName());
        claims.put("email", ((User) userDetails).getEmail());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(Duration.ofMinutes(jwtConfigProperties.getExpirationTimeMinutes()))))
                .signWith(getKey())

                .compact();
    }

    /**
     * Get the expiration date from the token
     *
     * @param token containing the expiration date to be retrieved
     * @return a Date object of expiration time of token
     */
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    /**
     * Checks if the given token has expired
     *
     * @param token to verify the expiration
     * @return true if the token has expired, otherwise false
     */
    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Get username if the token is valid
     * @param token extract the username
     * @return username if the token is valid else write in a log some error message
     */

    public String parseJwt(final String token) {
        String username = null;

        try {
            final var subj = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJwt(token);

            username = subj.getBody().getIssuer();
        } catch (JwtException e) {
            log.warn("JWT token validation error", e);
        }
        return username;
    }

    /**
     * Extracts the token from the 'Authorization' header in the HTTP request
     *
     * @param request HTTP request containing 'Authorization'
     * @return the extracted token from the request header
     * @throws Exception if some problem with 'Authorization' header
     */
    public String resolveToken(final HttpServletRequest request) throws Exception {
        final var bearer = Objects.requireNonNull(request.getHeader(HttpHeaders.AUTHORIZATION)); //

        final var parts = bearer.split(" ");
        if (parts.length != 2 || !"Bearer".equals(parts[0])) {
            throw new Exception("Incorrect authorization");
        }

        return parts[1];
    }

    /**
     * Validates the signature of the JWT token
     *
     * @param token to verify
     * @return true if the token's signature is valid, otherwise false
     */
    public boolean validateTokenSignature(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn("Invalid token signature", e);
            return false;
        }
    }

    /**
     * Generates a SecretKey for use in creating and validating JWT tokens
     *
     * This method get the secret key from the application configuration using JwtConfigProperties java class .getSecret method
     *
     * @return a SecretKey for signing and validating JWT tokens
     */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(jwtConfigProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}

