package com.osipov.server.service;

import com.osipov.server.dto.in.CommentCreateDto;
import com.osipov.server.dto.out.CommentDto;
import com.osipov.server.model.Client;

import java.util.List;

public interface CommentService {
    CommentDto createFromRequest(CommentCreateDto commentCreateRequest, Client author, Long taskId);

    List<CommentDto> getCommentsByTask(Long taskId);
}