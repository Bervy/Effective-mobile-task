package com.osipov.server.service.impl;

import com.osipov.server.model.BlacklistRefreshToken;
import com.osipov.server.repository.BlacklistRefreshTokenRepository;
import com.osipov.server.service.BlacklistRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlacklistRefreshTokenServiceImpl implements BlacklistRefreshTokenService {
    private final BlacklistRefreshTokenRepository blacklistRefreshTokenRepository;

    @Override
    @Transactional
    public BlacklistRefreshToken save(String jti) {
        return blacklistRefreshTokenRepository.save(
                BlacklistRefreshToken.builder()
                        .jti(jti)
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExists(String jti) {
        return blacklistRefreshTokenRepository.existsByJti(jti);
    }
}