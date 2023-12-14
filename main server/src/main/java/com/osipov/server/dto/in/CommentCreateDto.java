package com.osipov.server.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {
    @NotBlank
    @JsonProperty(value = "text", required = true)
    private String text;
}