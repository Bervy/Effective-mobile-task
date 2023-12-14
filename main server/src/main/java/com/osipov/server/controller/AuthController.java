package com.osipov.server.controller;

import com.osipov.server.dto.in.ClientLoginDto;
import com.osipov.server.dto.in.ClientRegistrationDto;
import com.osipov.server.dto.in.RefreshDto;
import com.osipov.server.dto.out.AccessAndRefreshJwtDto;
import com.osipov.server.dto.out.ClientDto;

import javax.validation.Valid;

public interface AuthController {
    ClientDto registration(@Valid ClientRegistrationDto clientRegistrationDto);

    AccessAndRefreshJwtDto login(@Valid ClientLoginDto userLoginDto);

    AccessAndRefreshJwtDto refresh(@Valid RefreshDto refreshDto);
}