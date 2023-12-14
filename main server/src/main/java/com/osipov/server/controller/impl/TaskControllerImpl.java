package com.osipov.server.controller.impl;

import com.osipov.server.controller.TaskController;
import com.osipov.server.dto.in.TaskCreateDto;
import com.osipov.server.dto.in.TaskUpdateDto;
import com.osipov.server.dto.in.TaskUpdateStatusDto;
import com.osipov.server.dto.out.TaskDto;
import com.osipov.server.dto.out.TaskWithCommentPaginationDto;
import com.osipov.server.error.NotFoundException;
import com.osipov.server.model.Client;
import com.osipov.server.service.impl.TaskServiceImpl;
import com.osipov.server.service.impl.TaskWithCommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tasks", description = "API for tasks")
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskControllerImpl implements TaskController {
    private final TaskServiceImpl taskService;
    private final TaskWithCommentServiceImpl taskWithCommentService;

    @Operation(summary = "Create task", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MethodArgumentNotValidException.class)
                    )
            )
    })

    @PostMapping
    @Override
    public TaskDto createTask(@RequestBody TaskCreateDto taskCreateDto,
                              @AuthenticationPrincipal Client client) {
        return taskService.createFromRequestWithAuthor(taskCreateDto, client);
    }

    @Operation(summary = "Update task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MethodArgumentNotValidException.class)
                    )
            )
    })
    @PutMapping("/{id}")
    @Override
    public TaskDto updateTask(@PathVariable Long id,
                              @RequestBody TaskUpdateDto taskUpdateDto,
                              @AuthenticationPrincipal Client client) {
        return taskService.updateFromRequestWithAuthor(id, taskUpdateDto, client);
    }

    @Operation(summary = "View task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task viewed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
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
    @GetMapping("/{id}")
    @Override
    public TaskDto getTask(@PathVariable Long id) {
        return taskService.get(id);
    }

    @Operation(summary = "Get list of tasks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Received a list of tasks",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskDto.class))
                    )
            )
    })
    @GetMapping("/list")
    @Override
    public List<TaskDto> getTaskList() {
        return taskService.getAll();
    }

    @Operation(summary = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task deleted"
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
    @DeleteMapping("/{id}")
    @Override
    public void deleteTask(@PathVariable Long id,
                           @AuthenticationPrincipal Client client) {
        taskService.deleteByIdAndAuthorId(id, client.getId());
    }

    @Operation(summary = "Change status of task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status changed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
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
    @PatchMapping("/{id}/status")
    @Override
    public TaskDto changeStatus(@PathVariable Long id,
                                @RequestBody TaskUpdateStatusDto taskUpdateStatusDto,
                                @AuthenticationPrincipal Client client) {
        return taskService.changeStatus(id, client.getId(), taskUpdateStatusDto.getStatus());
    }

    @Operation(summary = "Get tasks by author or performer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Received tasks by author or performer",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskWithCommentPaginationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author or performer not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)
                    )
            )
    })
    @GetMapping("/list/filter")
    @Override
    public TaskWithCommentPaginationDto getTaskWithFilters(
            @RequestParam(name = "author_id", required = false) Long authorId,
            @RequestParam(name = "performer_id", required = false) Long performerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return taskWithCommentService.getWithFilter(authorId, performerId, page, size);
    }
}