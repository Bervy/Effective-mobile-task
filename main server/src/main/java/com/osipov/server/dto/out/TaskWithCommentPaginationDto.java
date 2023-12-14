package com.osipov.server.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskWithCommentPaginationDto {
    @JsonProperty(value = "tasks")
    private List<TaskWithCommentDto> tasks;

    @JsonProperty(value = "current_page")
    private long currentPage;

    @JsonProperty(value = "total_items")
    private long totalItems;

    @JsonProperty(value = "total_pages")
    private long totalPages;
}
