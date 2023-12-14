package com.osipov.server.service.impl;

import com.osipov.server.dto.in.TaskCreateDto;
import com.osipov.server.dto.in.TaskUpdateDto;
import com.osipov.server.dto.out.TaskDto;
import com.osipov.server.error.AccessDeniedException;
import com.osipov.server.error.NotFoundException;
import com.osipov.server.mapper.ClientMapper;
import com.osipov.server.mapper.TaskMapper;
import com.osipov.server.model.Client;
import com.osipov.server.model.Task;
import com.osipov.server.model.enums.TaskStatus;
import com.osipov.server.repository.ClientRepository;
import com.osipov.server.repository.TaskRepository;
import com.osipov.server.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.osipov.server.error.ExceptionDescriptions.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ClientRepository clientRepository;
    private final ClientServiceImpl clientService;

    @Override
    @Transactional(readOnly = true)
    public TaskDto get(Long id) {
        return TaskMapper.taskToOutDto(getTaskById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDto getByIdAndAuthorId(Long id, Long authorId) {
        return TaskMapper.taskToOutDto(getTaskByIdAndAuthorId(id, authorId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getAll() {
        return TaskMapper.tasksToListOutDto(taskRepository.findAll());
    }

    @Override
    @Transactional
    public TaskDto createFromRequestWithAuthor(TaskCreateDto taskCreateDto, Client client) {
        Client performer = taskCreateDto.getPerformerId() != null ?
                ClientMapper.dtoOutToClient(clientService.getById(taskCreateDto.getPerformerId())) :
                null;
        Task task = Task.builder()
                .title(taskCreateDto.getTitle())
                .description(taskCreateDto.getDescription())
                .status(TaskStatus.PENDING)
                .priority(taskCreateDto.getPriority())
                .author(client)
                .performer(performer)
                .build();
        return TaskMapper.taskToOutDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto updateFromRequestWithAuthor(Long id, TaskUpdateDto taskUpdateDto, Client client) {
        Task task = getTaskByIdAndAuthorId(id, client.getId());
        Client performer = taskUpdateDto.getPerformerId() != null ?
                clientRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getTitle())) :
                null;
        task.setTitle(taskUpdateDto.getTitle());
        task.setDescription(taskUpdateDto.getDescription());
        task.setStatus(taskUpdateDto.getStatus());
        task.setPriority(taskUpdateDto.getPriority());
        task.setPerformer(performer);
        return TaskMapper.taskToOutDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto changeStatus(Long id, Long clientId, TaskStatus status) {
        Task task = getTaskById(id);
        if (!Objects.equals(task.getAuthor().getId(), clientId)
                && !Objects.equals(task.getPerformer().getId(), clientId)) {
            throw new AccessDeniedException(NOT_AUTHOR_AND_PERFORMER.getTitle());
        }
        task.setStatus(status);
        return TaskMapper.taskToOutDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteByIdAndAuthorId(Long id, Long authorId) {
        Task task = getTaskByIdAndAuthorId(id, authorId);
        taskRepository.delete(task);
    }

    private Task getTaskById(Long id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND.getTitle()));
    }

    private Task getTaskByIdAndAuthorId(Long id, Long authorId) {
        return taskRepository.findByIdAndAuthor_Id(id, authorId)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND.getTitle()));
    }
}