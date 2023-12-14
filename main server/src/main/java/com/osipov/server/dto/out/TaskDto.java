package com.osipov.server.dto.out;

import com.osipov.server.model.enums.TaskPriority;
import com.osipov.server.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "status")
    private TaskStatus status;

    @JsonProperty(value = "priority")
    private TaskPriority priority;

    @JsonProperty(value = "author")
    private ClientDto author;

    @JsonProperty(value = "performer")
    private ClientDto performer;
}
