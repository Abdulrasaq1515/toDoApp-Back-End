package com.toDoApp.controller;

import com.toDoApp.dto.request.TodoRequest;
import com.toDoApp.dto.request.UpdateTaskRequest;
import com.toDoApp.dto.response.TodoResponse;
import com.toDoApp.service.todo.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/{userId}")
    public ResponseEntity<TodoResponse> createTodo(
            @PathVariable String userId,
            @RequestBody TodoRequest request
    ) {
        TodoResponse response = todoService.createTodo(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TodoResponse>> getTodosByUser(@PathVariable String userId) {
        List<TodoResponse> responses = todoService.getTodosByUser(userId);
        return ResponseEntity.ok(responses);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable String id,
            @RequestBody TodoRequest request
    ) {
        TodoResponse response = todoService.updateTodo(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TodoResponse> updateTodoStatus(
            @PathVariable String id,
            @RequestParam boolean completed
    ) {
        TodoResponse response = todoService.updateTodoStatus(id, completed);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/task")
    public ResponseEntity<TodoResponse> updateTaskText(
            @PathVariable String id,
            @RequestBody UpdateTaskRequest request) {

        TodoResponse updated = todoService.updateTaskText(id, request.getTask());
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable String id) {
        boolean deleted = todoService.deleteTodo(id);
        return ResponseEntity.ok(deleted ? "Todo deleted successfully" : "Todo not found");
    }

    @GetMapping("/{userId}/search")
    public ResponseEntity<List<TodoResponse>> searchTodos(
            @PathVariable String userId,
            @RequestParam String query
    ) {
        List<TodoResponse> responses = todoService.searchTodos(userId, query);
        return ResponseEntity.ok(responses);
    }
}
