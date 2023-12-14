package com.osipov.server.mapper;

import com.osipov.server.dto.out.ClientDto;
import com.osipov.server.model.Client;

public class ClientMapper {
    public static ClientDto clientToDtoOut(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .email(client.getEmail())
                .build();
    }

    public static Client dtoOutToClient(ClientDto clientDto) {
        return Client.builder()
                .id(clientDto.getId())
                .email(clientDto.getEmail())
                .build();
    }
}