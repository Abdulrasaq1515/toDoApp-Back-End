package com.toDoApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ErrorResponse {
    private String message;
    private int status;
    private long timestamp;

}
