package com.toDoApp.controller;

import com.toDoApp.dto.request.TodoRequest;
import com.toDoApp.dto.request.UpdateTaskRequest;
import com.toDoApp.dto.response.TodoResponse;
import com.toDoApp.service.todo.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createTodo(@PathVariable String userId, @RequestBody TodoRequest request) {
        try {
            TodoResponse response = todoService.createTodo(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Failed to create todo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
        }
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getTodosByUser(@PathVariable String userId) {
        try {
            List<TodoResponse> responses = todoService.getTodosByUser(userId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Failed to fetch todos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable String id, @RequestBody TodoRequest request) {
        try {
            TodoResponse response = todoService.updateTodo(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateTodoStatus(@PathVariable String id, @RequestParam boolean completed) {
        try {
            TodoResponse response = todoService.updateTodoStatus(id, completed);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }
    }
    @PatchMapping("/{id}/task")
    public ResponseEntity<?> updateTaskText(@PathVariable String id, @RequestBody UpdateTaskRequest request) {
        try {
            TodoResponse updated = todoService.updateTaskText(id, request.getTask());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable String id) {
        try {
            boolean deleted = todoService.deleteTodo(id);
            if (deleted) {
                return ResponseEntity.ok("Todo deleted successfully");
            } else {
                List<String> errors = new ArrayList<>();
                errors.add("Todo not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
            }
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }
    }
    @GetMapping("/{userId}/search")
    public ResponseEntity<?> searchTodos(@PathVariable String userId, @RequestParam String query
    ) {
        try {
            List<TodoResponse> responses = todoService.searchTodos(userId, query);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Failed to search todos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
        }
    }
    //  validation exception handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        for (ObjectError error : allErrors) {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}