package com.osipov.server.controller.impl;

import com.osipov.server.controller.AuthController;
import com.osipov.server.dto.in.ClientLoginDto;
import com.osipov.server.dto.in.ClientRegistrationDto;
import com.osipov.server.dto.in.RefreshDto;
import com.osipov.server.dto.out.AccessAndRefreshJwtDto;
import com.osipov.server.dto.out.ClientDto;
import com.osipov.server.service.impl.ClientServiceImpl;
import com.osipov.server.service.impl.JwtServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

@Tag(name = "Auth", description = "API for registration and generation JWT")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final ClientServiceImpl clientService;
    private final JwtServiceImpl jwtService;

    @Operation(summary = "Registration")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InvalidParameterException.class)
                    )
            )
    })
    @PostMapping("/registration")
    @Override
    public ClientDto registration(@RequestBody ClientRegistrationDto clientRegistrationDto) {
        return clientService.create(clientRegistrationDto);
    }

    @Operation(summary = "Get access JWT and refresh JWT")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Received access JWT and refresh JWT",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessAndRefreshJwtDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InvalidParameterException.class)
                    )
            )
    })
    @PostMapping("/login")
    @Override
    public AccessAndRefreshJwtDto login(@RequestBody ClientLoginDto userLoginDto) {
        return jwtService.createAccessAndRefreshTokens(userLoginDto);
    }

    @Operation(summary = "Update tokens")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tokens updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessAndRefreshJwtDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InvalidParameterException.class)
                    )
            )
    })
    @PostMapping("/refresh")
    @Override
    public AccessAndRefreshJwtDto refresh(@RequestBody RefreshDto refreshDto) {
        return jwtService.refreshTokens(refreshDto);
    }
}