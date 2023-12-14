package com.osipov.server.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshDto {
    @NotBlank
    @JsonProperty(value = "refresh_token", required = true)
    private String refreshToken;
}