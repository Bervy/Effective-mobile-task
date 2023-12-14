package com.osipov.server.controller.impl;

import com.osipov.server.controller.CommentController;
import com.osipov.server.dto.in.CommentCreateDto;
import com.osipov.server.dto.out.CommentDto;
import com.osipov.server.error.NotFoundException;
import com.osipov.server.model.Client;
import com.osipov.server.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comments", description = "API for comments")
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentControllerImpl implements CommentController {
    private final CommentServiceImpl commentService;

    @Operation(summary = "Write a comment to the task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment written",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)
                    )
            )
    })
    @PostMapping("/{task_id}")
    @Override
    public CommentDto createComment(@PathVariable(value = "task_id") Long taskId,
                                    @RequestBody CommentCreateDto commentCreateDto,
                                    @AuthenticationPrincipal Client client) {
        return commentService.createFromRequest(commentCreateDto, client, taskId);
    }
}