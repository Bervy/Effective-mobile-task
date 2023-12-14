package com.osipov.server.service;

import com.osipov.server.dto.TokenBody;
import com.osipov.server.dto.in.ClientLoginDto;
import com.osipov.server.dto.in.RefreshDto;
import com.osipov.server.dto.out.AccessAndRefreshJwtDto;

public interface JwtService {
    TokenBody getTokenBody(String token);
    AccessAndRefreshJwtDto createAccessAndRefreshTokens(ClientLoginDto userLoginRequest);
    AccessAndRefreshJwtDto refreshTokens(RefreshDto refreshRequest);
}