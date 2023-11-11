package com.greenfoxacademy.foxshopnullpointerninjasotocyon.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.BlacklistedJWTToken;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;


@Component
@RequiredArgsConstructor
public class DeleteExpiredToken {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final TokenBlacklistRepository tokenBlacklistRepository;

    //implementation within buld.gradle -> dependencies:   implementation 'com.auth0:java-jwt:4.4.0'
    public Date extractExpirationDate(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }

    public long expiresWithinSeconds(String token) {
        Date expirationDate = extractExpirationDate(token);
        Date dateNow = new Date();
        long differenceMiliseconds = expirationDate.getTime() - dateNow.getTime();
        return differenceMiliseconds / 1000;
    }

   public void deleteAfterExpiration(String token) {
        final Runnable runDeleteMethod = new Runnable() {
            @Override
            public void run() {
                tokenBlacklistRepository.delete(new BlacklistedJWTToken(token));
            }
        };
        final ScheduledFuture<?> deleteTimer = scheduler.schedule(runDeleteMethod, expiresWithinSeconds(token) + (60 * 5), SECONDS);
    }
}
