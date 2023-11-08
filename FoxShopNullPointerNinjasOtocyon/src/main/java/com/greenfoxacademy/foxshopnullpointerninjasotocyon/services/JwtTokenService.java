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
import org.springframework.security.core.GrantedAuthority;
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

    public String generateToken(final UserDetails userDetails) {

        final var now = Instant.now();

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

    public Date getExpirationDateFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
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

    public String resolveToken(final HttpServletRequest request) throws Exception {
        final var bearer = Objects.requireNonNull(request.getHeader(HttpHeaders.AUTHORIZATION));

        final var parts = bearer.split(" ");
        if (parts.length != 2 || !"Bearer".equals(parts[0])) {
            throw new Exception("Incorrect authorization");
        }

        return parts[1];
    }


    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(jwtConfigProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}

