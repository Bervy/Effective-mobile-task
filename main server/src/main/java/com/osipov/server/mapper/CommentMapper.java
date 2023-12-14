package com.osipov.server.mapper;

import com.osipov.server.dto.out.ClientDto;
import com.osipov.server.dto.out.CommentDto;
import com.osipov.server.model.Comment;

import java.util.List;


public class CommentMapper {
    public static CommentDto commentToDtoOut(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(ClientDto.builder()
                        .id(comment.getAuthor().getId())
                        .email(comment.getAuthor().getEmail())
                        .build())
                .taskId(comment.getTask().getId())
                .build();
    }

    public static List<CommentDto> commentsToListOutDto(List<Comment> comments) {
        return comments.stream().map(CommentMapper::commentToDtoOut).toList();
    }
}