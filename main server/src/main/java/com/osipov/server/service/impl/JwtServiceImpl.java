package com.osipov.server.service.impl;

import com.osipov.server.dto.TokenBody;
import com.osipov.server.dto.in.ClientLoginDto;
import com.osipov.server.dto.in.RefreshDto;
import com.osipov.server.dto.out.AccessAndRefreshJwtDto;
import com.osipov.server.error.AccessDeniedException;
import com.osipov.server.error.InvalidTokenException;
import com.osipov.server.error.NotFoundException;
import com.osipov.server.model.Client;
import com.osipov.server.repository.ClientRepository;
import com.osipov.server.service.JwtService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerErrorException;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import static com.osipov.server.error.ExceptionDescriptions.PASSWORD_MISMATCH;
import static com.osipov.server.error.ExceptionDescriptions.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private static final String PRIVATE_KEY = "asdaAASDFADFEASFADF9786ASDFADF86ADF";
    private static final String PUBLIC_KEY = "ADSFdSF879&SDFSDFSDF897987";
    private static final long ACCESS_TOKEN_EXPIRATION = 900;
    private static final long REFRESH_TOKEN_EXPIRATION = 2592000;
    private final BlacklistRefreshTokenServiceImpl blacklistRefreshTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ClientRepository clientRepository;

    @Override
    public TokenBody getTokenBody(String token) {
        Claims claims = getClaimsIfTokenValid(token);

        try {
            return TokenBody.builder()
                    .clientId(claims.get("user_id", Long.class))
                    .email(claims.get("email", String.class))
                    .tokenType(claims.get("token_type", String.class))
                    .jti(claims.getId())
                    .issuedAt(claims.getIssuedAt())
                    .expiration(claims.getExpiration())
                    .build();
        } catch (RequiredTypeException exception) {
            throw new InvalidTokenException();
        }
    }

    @Override
    @Transactional
    public AccessAndRefreshJwtDto createAccessAndRefreshTokens(ClientLoginDto clientLoginDto) {
        Client user = getClientIfPasswordCorrect(clientLoginDto);

        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);

        return AccessAndRefreshJwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public AccessAndRefreshJwtDto refreshTokens(RefreshDto refreshDto) {
        TokenBody tokenBody = getTokenBody(refreshDto.getRefreshToken());

        if (!tokenBody.getTokenType().equals("refresh")) {
            throw new InvalidTokenException();
        }

        if (blacklistRefreshTokenService.isExists(tokenBody.getJti())) {
            throw new InvalidTokenException("This refresh token is invalid");
        }

        Client client = clientRepository
                .findById(tokenBody.getClientId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getTitle()));

        String accessToken = createAccessToken(client);
        String refreshToken = createRefreshToken(client);

        blacklistRefreshTokenService.save(tokenBody.getJti());

        return AccessAndRefreshJwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String createAccessToken(Client client) {
        return buildToken(new HashMap<>(), client, "access", ACCESS_TOKEN_EXPIRATION);
    }

    private String createRefreshToken(Client client) {
        return buildToken(new HashMap<>(), client, "refresh", REFRESH_TOKEN_EXPIRATION);
    }

    private String buildToken(Map<String, Object> extraClaims, Client clientDetails, String type, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setHeaderParam("typ", "JWT")
                .claim("user_id", clientDetails.getId())
                .claim("email", clientDetails.getUsername())
                .claim("token_type", type)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS512)
                .compact();
    }

    private Claims getClaimsIfTokenValid(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException |
                 UnsupportedJwtException |
                 MalformedJwtException |
                 SignatureException |
                 IllegalArgumentException exception) {
            throw new InvalidTokenException();
        }
    }

    private Client getClientIfPasswordCorrect(ClientLoginDto clientLoginDto) {
        Client client;

        client = clientRepository
                .findByEmail(clientLoginDto.getEmail())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getTitle()));

        if (!bCryptPasswordEncoder.matches(clientLoginDto.getPassword(), client.getPassword())) {
            throw new AccessDeniedException(PASSWORD_MISMATCH.getTitle());
        }

        return client;
    }

    private Key getPrivateKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytes = Base64.getDecoder().decode(PRIVATE_KEY);
            PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(bytes);

            return keyFactory.generatePrivate(keySpecPv);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServerErrorException(e.getMessage());
        }
    }

    private Key getPublicKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytes = Base64.getDecoder().decode(PUBLIC_KEY);
            X509EncodedKeySpec keySpecPv = new X509EncodedKeySpec(bytes);

            return keyFactory.generatePublic(keySpecPv);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServerErrorException(e.getMessage());
        }
    }
}