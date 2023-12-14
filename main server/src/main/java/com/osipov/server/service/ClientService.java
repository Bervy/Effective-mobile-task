package com.osipov.server.service;

import com.osipov.server.dto.TokenBody;
import com.osipov.server.dto.in.ClientRegistrationDto;
import com.osipov.server.dto.out.ClientDto;

public interface ClientService {
    ClientDto create(ClientRegistrationDto clientFromRequest);

    ClientDto getById(Long id);

    ClientDto getByEmail(String email);

    ClientDto getByIdFromToken(TokenBody tokenBody);

    void deleteById(Long id);

    boolean userIsExists(String email);
}
