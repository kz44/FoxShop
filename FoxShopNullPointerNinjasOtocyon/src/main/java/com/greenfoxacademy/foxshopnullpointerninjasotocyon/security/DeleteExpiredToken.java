package com.greenfoxacademy.foxshopnullpointerninjasotocyon.security;

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

    private final JwtTokenService jwtTokenService;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public long expiresWithinSeconds(String token) {
        Date expirationDate = jwtTokenService.getExpirationDateFromToken(token);
        Date dateNow = new Date();
        long differenceMilliseconds = expirationDate.getTime() - dateNow.getTime();
        return differenceMilliseconds / 1000;
    }

   public void deleteAfterExpiration(String token) {
        final Runnable runDeleteMethod = new Runnable() {
            @Override
            public void run() {

                BlacklistedJWTToken blacklistedToken = tokenBlacklistRepository.findDistinctByToken(token);
                if (token != null) {
                    tokenBlacklistRepository.delete(blacklistedToken);
                }   }
        };
        final ScheduledFuture<?> deleteTimer = scheduler.schedule(runDeleteMethod, expiresWithinSeconds(token) + (60 * 5), SECONDS);
    }
}
