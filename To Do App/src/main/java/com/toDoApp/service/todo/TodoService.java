package com.toDoApp.service.todo;

import com.toDoApp.dto.request.TodoRequest;
import com.toDoApp.dto.response.TodoResponse;

import java.util.List;

public interface TodoService {
    TodoResponse createTodo(String userId, TodoRequest request);
    List<TodoResponse> getTodosByUser(String userId);
    TodoResponse updateTodo(String id, TodoRequest request);
    TodoResponse updateTodoStatus(String id, boolean completed);
    boolean deleteTodo(String id);
    List<TodoResponse> searchTodos(String userId, String search);
    TodoResponse updateTaskText(String todoId, String newTask);

}
