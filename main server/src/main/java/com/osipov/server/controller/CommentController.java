package com.osipov.server.controller;

import com.osipov.server.dto.in.CommentCreateDto;
import com.osipov.server.dto.out.CommentDto;
import com.osipov.server.model.Client;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

public interface CommentController {
    CommentDto createComment(@Positive Long taskId, @Valid CommentCreateDto commentCreateDto, @Valid Client client);
}