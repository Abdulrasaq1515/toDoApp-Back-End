package com.toDoApp.controller;

import com.toDoApp.dto.request.TodoRequest;
import com.toDoApp.dto.request.UpdateTaskRequest;
import com.toDoApp.dto.response.TodoResponse;
import com.toDoApp.exception.TodoNotFoundException;
import com.toDoApp.exception.TokenNotFoundException;
import com.toDoApp.service.todo.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createTodo(@PathVariable String userId, @RequestBody TodoRequest request) {
        TodoResponse response = todoService.createTodo(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getTodosByUser(@PathVariable String userId) {
        List<TodoResponse> responses = todoService.getTodosByUser(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable String id, @RequestBody TodoRequest request) {
        TodoResponse response = todoService.updateTodo(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateTodoStatus(@PathVariable String id, @RequestParam boolean completed) {
        TodoResponse response = todoService.updateTodoStatus(id, completed);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/task")
    public ResponseEntity<?> updateTaskText(@PathVariable String id, @RequestBody UpdateTaskRequest request) {
        TodoResponse updated = todoService.updateTaskText(id, request.getTask());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable String id) {
        boolean deleted = todoService.deleteTodo(id);
        if (deleted) {
            return ResponseEntity.ok("Todo deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo not found");
    }

    @GetMapping("/{userId}/search")
    public ResponseEntity<?> searchTodos(@PathVariable String userId, @RequestParam String query) {
        List<TodoResponse> responses = todoService.searchTodos(userId, query);
        return ResponseEntity.ok(responses);
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<String> handleTodoNotFound(TodoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<String> handleTokenNotFound(TokenNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allErrors.size(); i++) {
            ObjectError error = allErrors.get(i);
            String msg = error.getDefaultMessage();
            sb.append(msg != null ? msg : "Validation error");
            if (i < allErrors.size() - 1) {
                sb.append("; ");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error: " + ex.getMessage());
    }
}