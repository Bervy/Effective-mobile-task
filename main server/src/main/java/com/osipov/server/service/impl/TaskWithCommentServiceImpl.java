package com.osipov.server.service.impl;
import com.osipov.server.dto.out.ClientDto;
import com.osipov.server.dto.out.CommentDto;
import com.osipov.server.dto.out.TaskWithCommentDto;
import com.osipov.server.dto.out.TaskWithCommentPaginationDto;
import com.osipov.server.model.Task;
import com.osipov.server.repository.TaskRepository;
import com.osipov.server.service.TaskWithCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskWithCommentServiceImpl implements TaskWithCommentService {
    private final TaskRepository taskRepository;
    private final CommentServiceImpl commentService;

    @Override
    @Transactional(readOnly = true)
    public TaskWithCommentPaginationDto getWithFilter(Long authorId, Long performerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage;

        if (authorId == null && performerId == null) {
            taskPage = taskRepository.findAll(pageable);
        } else if (authorId != null && performerId != null) {
            taskPage = taskRepository.findAllByAuthor_IdAndPerformer_Id(authorId, performerId, pageable);
        } else if (authorId != null) {
            taskPage = taskRepository.findAllByAuthor_Id(authorId, pageable);
        } else {
            taskPage = taskRepository.findAllByPerformer_Id(performerId, pageable);
        }

        List<Task> tasks = taskPage.getContent();

        return TaskWithCommentPaginationDto.builder()
                .tasks(
                        tasks.stream()
                                .map(task -> TaskWithCommentDto.builder()
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
                                        .comments(commentService.getCommentsByTask(task.getId()).stream()
                                                .map(comment -> CommentDto.builder()
                                                        .id(comment.getId())
                                                        .text(comment.getText())
                                                        .author(ClientDto.builder()
                                                                .id(comment.getAuthor().getId())
                                                                .email(comment.getAuthor().getEmail())
                                                                .build())
                                                        .taskId(comment.getTaskId())
                                                        .build())
                                                .toList())
                                        .build())
                                .toList()
                )
                .currentPage(taskPage.getNumber())
                .totalItems(taskPage.getTotalElements())
                .totalPages(taskPage.getTotalPages())
                .build();
    }
}