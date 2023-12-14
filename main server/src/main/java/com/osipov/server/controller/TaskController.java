package com.osipov.server.controller;

import com.osipov.server.dto.in.TaskCreateDto;
import com.osipov.server.dto.in.TaskUpdateDto;
import com.osipov.server.dto.in.TaskUpdateStatusDto;
import com.osipov.server.dto.out.TaskDto;
import com.osipov.server.dto.out.TaskWithCommentPaginationDto;
import com.osipov.server.model.Client;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

public interface TaskController {
    TaskDto createTask(@Valid TaskCreateDto taskCreatedTO, @Valid Client client);

    TaskDto updateTask(@Positive Long id, @Valid TaskUpdateDto taskUpdateDto, @Valid Client client);

    TaskDto getTask(@Positive Long id);

    List<TaskDto> getTaskList();

    void deleteTask(@Positive Long id, @Valid Client client);

    TaskDto changeStatus(@Positive Long id, @Valid TaskUpdateStatusDto taskUpdateStatusDto, @Valid Client client);

    TaskWithCommentPaginationDto getTaskWithFilters(@Positive Long authorId, @Positive Long performerId
            , @Positive int page, @Positive int size);
}