package com.osipov.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenBody {
    private Long clientId;
    private String email;
    private String tokenType;
    private String jti;
    private Date issuedAt;
    private Date expiration;
}