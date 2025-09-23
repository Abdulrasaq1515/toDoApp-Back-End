package com.toDoApp.exception;

public class TodoAccessDeniedException extends RuntimeException {
    public TodoAccessDeniedException(String message) {
        super(message);
    }
}
