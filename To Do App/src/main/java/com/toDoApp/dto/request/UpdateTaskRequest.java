package com.toDoApp.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskRequest {
    private String task;
}
