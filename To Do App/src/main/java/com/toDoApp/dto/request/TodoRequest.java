package com.toDoApp.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoRequest {
    @NotNull(message = "Task is required")
    private String task;
    @NotNull(message = "Completed is required")
    private boolean completed;
    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;
}


