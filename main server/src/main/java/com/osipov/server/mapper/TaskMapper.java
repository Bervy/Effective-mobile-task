package com.osipov.server.mapper;

import com.osipov.server.dto.out.ClientDto;
import com.osipov.server.dto.out.TaskDto;
import com.osipov.server.model.Task;

import java.util.List;

public class TaskMapper {
    public static TaskDto taskToOutDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(ClientDto.builder()
                        .id(task.getAuthor().getId())
                        .email(task.getAuthor().getEmail())
                        .build())
                .performer(ClientDto.builder()
                        .id(task.getPerformer().getId())
                        .email(task.getPerformer().getEmail())
                        .build())
                .build();
    }

    public static List<TaskDto> tasksToListOutDto(List<Task> tasks) {
        return tasks.stream().map(TaskMapper::taskToOutDto).toList();
    }
}