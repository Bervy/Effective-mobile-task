package com.osipov.server.service.impl;

import com.osipov.server.dto.TokenBody;
import com.osipov.server.dto.in.ClientRegistrationDto;
import com.osipov.server.dto.out.ClientDto;
import com.osipov.server.error.AccessDeniedException;
import com.osipov.server.error.ConflictException;
import com.osipov.server.error.NotFoundException;
import com.osipov.server.mapper.ClientMapper;
import com.osipov.server.model.Client;
import com.osipov.server.model.enums.Role;
import com.osipov.server.repository.ClientRepository;
import com.osipov.server.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.osipov.server.error.ExceptionDescriptions.*;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public ClientDto create(ClientRegistrationDto clientRegistrationDto) {
        if (!clientRegistrationDto.getPassword().equals(clientRegistrationDto.getPasswordConfirm())) {
            throw new AccessDeniedException(PASSWORD_MISMATCH.getTitle());
        }

        if (userIsExists(clientRegistrationDto.getEmail())) {
            throw new ConflictException(USER_ALREADY_EXISTS.getTitle());
        }

        Client u = Client.builder()
                .email(clientRegistrationDto.getEmail())
                .password(bCryptPasswordEncoder.encode(clientRegistrationDto.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        return ClientMapper.clientToDtoOut(clientRepository.save(u));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto getById(Long id) {
        return ClientMapper.clientToDtoOut(getClientById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto getByEmail(String email) {
        return ClientMapper
                .clientToDtoOut(clientRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getTitle())));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto getByIdFromToken(TokenBody tokenBody) {
        return ClientMapper.clientToDtoOut(getClientById(tokenBody.getClientId()));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsExists(String email) {
        return clientRepository.findByEmail(email).isPresent();
    }

    private Client getClientById(Long id) {
        return clientRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getTitle()));
    }
}