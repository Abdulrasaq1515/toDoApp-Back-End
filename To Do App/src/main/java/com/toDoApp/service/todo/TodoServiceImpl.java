package com.toDoApp.service.todo;

import com.toDoApp.data.model.Todo;
import com.toDoApp.data.repository.TodoRepository;
import com.toDoApp.dto.request.TodoRequest;
import com.toDoApp.dto.response.TodoResponse;
import com.toDoApp.util.TodoMapper;
import com.toDoApp.util.TodoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public TodoResponse createTodo(String userId, TodoRequest request) {
        TodoValidator.validateTaskNotEmpty(request.getTask());
        Todo todo = TodoMapper.toTodo(userId, request.getTask(), request.isCompleted(), request.getDueDate());
        Todo saved = todoRepository.save(todo);
        return TodoMapper.toResponse(saved);
    }
    
    @Override
    public List<TodoResponse> getTodosByUser(String userId) {
        List<Todo> todos = todoRepository.findByUserId(userId);
        return TodoMapper.toResponseList(todos);
    }

    @Override
    public TodoResponse updateTodo(String id, TodoRequest request) {
        Todo existing = TodoValidator.validateTodoExists(id, todoRepository);
        TodoMapper.updateTodo(existing, request.getTask(), request.isCompleted(), request.getDueDate());
        Todo updated = todoRepository.save(existing);
        return TodoMapper.toResponse(updated);
    }

    @Override
    public TodoResponse updateTodoStatus(String id, boolean completed) {
        Todo existing = TodoValidator.validateTodoExists(id, todoRepository);
        existing.setCompleted(completed);
        existing.setUpdatedAt(LocalDateTime.now());
        Todo updated = todoRepository.save(existing);
        return TodoMapper.toResponse(updated);
    }
    @Override
    public TodoResponse updateTaskText(String todoId, String newTask) {
        TodoValidator.validateTaskNotEmpty(newTask);
        Todo todo = TodoValidator.validateTodoExists(todoId, todoRepository);
        todo.setTask(newTask);
        Todo saved = todoRepository.save(todo);
        return TodoMapper.toResponse(saved);
    }
    @Override
    public boolean deleteTodo(String id) {
        TodoValidator.validateTodoExistsForDelete(id, todoRepository);
        todoRepository.deleteById(id);
        return true;
    }
    @Override
    public List<TodoResponse> searchTodos(String userId, String search) {
        List<Todo> todos = todoRepository.findByUserIdAndTaskContainingIgnoreCase(userId, search);
        return TodoMapper.toResponseList(todos);
    }
}