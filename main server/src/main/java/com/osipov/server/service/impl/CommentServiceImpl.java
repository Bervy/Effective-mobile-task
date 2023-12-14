package com.osipov.server.service.impl;

import com.osipov.server.dto.in.CommentCreateDto;
import com.osipov.server.dto.out.CommentDto;
import com.osipov.server.error.NotFoundException;
import com.osipov.server.mapper.CommentMapper;
import com.osipov.server.model.Client;
import com.osipov.server.model.Comment;
import com.osipov.server.model.Task;
import com.osipov.server.repository.CommentRepository;
import com.osipov.server.repository.TaskRepository;
import com.osipov.server.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.osipov.server.error.ExceptionDescriptions.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public CommentDto createFromRequest(CommentCreateDto commentCreateDto, Client author, Long taskId) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND.getTitle()));

        Comment comment = Comment.builder()
                .text(commentCreateDto.getText())
                .author(author)
                .task(task)
                .build();

        return CommentMapper.commentToDtoOut(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByTask(Long taskId) {
        return CommentMapper.commentsToListOutDto(commentRepository.getCommentByTask_Id(taskId));
    }
}