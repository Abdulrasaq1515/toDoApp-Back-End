package com.toDoApp.util;

import com.toDoApp.data.model.Todo;
import com.toDoApp.data.repository.TodoRepository;
import com.toDoApp.exception.TodoNotFoundException;

import java.util.Optional;

public class TodoValidator {

    public static Todo validateTodoExists(String todoId, TodoRepository todoRepository) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (optionalTodo.isEmpty()) {
            throw new TodoNotFoundException("Todo with id " + todoId + " not found");
        }
        return optionalTodo.get();
    }
    public static void validateTodoExistsForDelete(String todoId, TodoRepository todoRepository) {
        if (!todoRepository.existsById(todoId)) {
            throw new TodoNotFoundException("Todo with id " + todoId + " not found");
        }
    }
    public static void validateTaskNotEmpty(String task) {
        if (task == null || task.trim().isEmpty()) {
            throw new IllegalArgumentException("Task cannot be empty");
        }
    }
}