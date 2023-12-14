package com.osipov.server.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRegistrationDto {
    @NotBlank
    @JsonProperty(value = "email", required = true)
    private String email;

    @NotBlank
    @JsonProperty(value = "password", required = true)
    private String password;

    @NotBlank
    @JsonProperty(value = "password_confirm", required = true)
    private String passwordConfirm;
}