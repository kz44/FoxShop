package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.BlacklistedJWTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.token.Token;

public interface TokenBlacklistRepository extends JpaRepository<BlacklistedJWTToken, Long> {
    BlacklistedJWTToken findDistinctByToken(String token);
    boolean existsByToken (String token);
}
