package com.osipov.server.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.osipov.server.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateStatusDto {
    @NotBlank
    @JsonProperty(value = "status", required = true)
    private TaskStatus status;
}