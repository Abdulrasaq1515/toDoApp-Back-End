package com.toDoApp.util;

import com.toDoApp.data.model.Todo;
import com.toDoApp.dto.response.TodoResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TodoMapper {

    public static Todo toTodo(String userId, String task, boolean completed, LocalDateTime dueDate) {
        Todo todo = new Todo();
        todo.setUserId(userId);
        todo.setTask(task);
        todo.setCompleted(completed);
        todo.setDueDate(dueDate);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
        return todo;
    }

    public static void updateTodo(Todo existing, String task, boolean completed, LocalDateTime dueDate) {
        if (task != null && !task.isBlank()) {
            existing.setTask(task);
        }
        existing.setCompleted(completed);
        if (dueDate != null) {
            existing.setDueDate(dueDate);
        }
        existing.setUpdatedAt(LocalDateTime.now());
    }

    public static TodoResponse toResponse(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .userId(todo.getUserId())
                .task(todo.getTask())
                .completed(todo.isCompleted())
                .dueDate(todo.getDueDate())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }

    public static List<TodoResponse> toResponseList(List<Todo> todos) {
        List<TodoResponse> responses = new ArrayList<>();
        if (todos != null) {
            for (Todo todo : todos) {
                responses.add(toResponse(todo));
            }
        }
        return responses;
    }

}
