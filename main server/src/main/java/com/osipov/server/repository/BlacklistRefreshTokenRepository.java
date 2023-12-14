package com.osipov.server.repository;

import com.osipov.server.model.BlacklistRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRefreshTokenRepository extends JpaRepository<BlacklistRefreshToken, Long> {
    boolean existsByJti(String jti);
}