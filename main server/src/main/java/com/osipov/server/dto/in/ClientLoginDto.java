package com.osipov.server.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientLoginDto {
    @NotBlank
    @JsonProperty(value = "email", required = true)
    private String email;

    @NotBlank
    @JsonProperty(value = "password", required = true)
    private String password;
}