package com.osipov.server.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.osipov.server.model.enums.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDto {
    @NotBlank
    @JsonProperty(value = "title", required = true)
    private String title;

    @NotBlank
    @JsonProperty(value = "description", required = true)
    private String description;

    @NotNull
    @JsonProperty(value = "priority", required = true)
    private TaskPriority priority;

    @JsonProperty(value = "performer_id", required = true)
    private Long performerId;
}