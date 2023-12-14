package com.osipov.server.service;

import com.osipov.server.dto.in.TaskCreateDto;
import com.osipov.server.dto.in.TaskUpdateDto;
import com.osipov.server.dto.out.TaskDto;
import com.osipov.server.model.Client;
import com.osipov.server.model.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskDto get(Long id);

    TaskDto getByIdAndAuthorId(Long id, Long authorId);

    List<TaskDto> getAll();

    TaskDto createFromRequestWithAuthor(TaskCreateDto taskCreateRequest, Client client);

    TaskDto updateFromRequestWithAuthor(Long id, TaskUpdateDto taskUpdateRequest, Client client);

    TaskDto changeStatus(Long id, Long clientId, TaskStatus status);

    void deleteByIdAndAuthorId(Long id, Long authorId);
}