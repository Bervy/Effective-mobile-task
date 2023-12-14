package com.osipov.server.service;

import com.osipov.server.model.BlacklistRefreshToken;

public interface BlacklistRefreshTokenService {
    BlacklistRefreshToken save(String jti);

    boolean isExists(String jti);
}