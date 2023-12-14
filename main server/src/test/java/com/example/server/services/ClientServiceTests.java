package com.example.server.services;

import com.osipov.server.dto.in.ClientRegistrationDto;
import com.osipov.server.error.AccessDeniedException;
import com.osipov.server.error.ConflictException;
import com.osipov.server.mapper.ClientMapper;
import com.osipov.server.model.Client;
import com.osipov.server.model.enums.Role;
import com.osipov.server.repository.ClientRepository;
import com.osipov.server.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.osipov.server.error.ExceptionDescriptions.PASSWORD_MISMATCH;
import static com.osipov.server.error.ExceptionDescriptions.USER_ALREADY_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ClientServiceTests {
    ClientRegistrationDto clientRegistrationRequest;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setup() {
        clientRegistrationRequest = ClientRegistrationDto.builder()
                .email("user@user.com")
                .password("321")
                .passwordConfirm("321")
                .build();
    }

    @Test
    void createWithDifferentPasswordTest() {
        clientRegistrationRequest.setPasswordConfirm("123");
        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> clientService.create(clientRegistrationRequest));

        assertEquals(PASSWORD_MISMATCH.getTitle(), exception.getMessage());
    }

    @Test
    void createWhenUserIsExistsTest() {
        Client client = new Client(1L, "user@user.com", "passwd", Role.ROLE_USER);
        Mockito.doReturn(Optional.of(client)).when(clientRepository).findByEmail(any());

        ConflictException exception = assertThrows(ConflictException.class,
                () -> clientService.create(clientRegistrationRequest));

        assertEquals(USER_ALREADY_EXISTS.getTitle(), exception.getMessage());
    }

    @Test
    void createUserAccessTest() {
        Client client = new Client(1L, "user1@user1.com", "password", Role.ROLE_USER);
        Mockito.doReturn(Optional.empty()).when(clientRepository).findByEmail(any());
        Mockito.doReturn(client).when(clientRepository).save(any());
        Mockito.doReturn("password").when(bCryptPasswordEncoder).encode(any());

        Client saveClient = ClientMapper.dtoOutToClient(clientService.create(clientRegistrationRequest));
        assertEquals(client, saveClient);
    }
}