package com.osipov.server.service;

import com.osipov.server.dto.out.TaskWithCommentPaginationDto;

public interface TaskWithCommentService {
    TaskWithCommentPaginationDto getWithFilter(Long authorId, Long performerId, int page, int size);
}