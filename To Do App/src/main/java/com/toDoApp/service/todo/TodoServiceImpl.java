package com.toDoApp.service.todo;

import com.toDoApp.data.model.Todo;
import com.toDoApp.data.repository.TodoRepository;
import com.toDoApp.dto.request.TodoRequest;
import com.toDoApp.dto.response.TodoResponse;
import com.toDoApp.exception.TodoNotFoundException;
import com.toDoApp.util.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private  TodoRepository todoRepository;

    @Override
    public TodoResponse createTodo(String userId, TodoRequest request) {
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
        Optional<Todo> optionTodo = todoRepository.findById(id);
        if (!optionTodo.isPresent()) {
            throw new TodoNotFoundException("Todo with id " + id + " not found");
        }
        Todo existing = optionTodo.get();
        TodoMapper.updateTodo(existing, request.getTask(), request.isCompleted(), request.getDueDate());
        Todo updated = todoRepository.save(existing);
        return TodoMapper.toResponse(updated);
    }

    @Override
    public TodoResponse updateTodoStatus(String id, boolean completed) {
        Optional<Todo> optionTodo = todoRepository.findById(id);
        if (!optionTodo.isPresent()) {
            throw new TodoNotFoundException("Todo with id " + id + " not found");
        }
        Todo existing = optionTodo.get();
        existing.setCompleted(completed);
        existing.setUpdatedAt(LocalDateTime.now());
        Todo updated = todoRepository.save(existing);
        return TodoMapper.toResponse(updated);
    }

    @Override
    public TodoResponse updateTaskText(String todoId, String newTask) {
        Optional<Todo> optionTodo = todoRepository.findById(todoId);
        if (optionTodo.isEmpty()) {
            throw new TodoNotFoundException("Todo not found with id: " + todoId);
        }
        Todo todo = optionTodo.get();
        if (newTask != null && !newTask.isBlank()) {
            todo.setTask(newTask);
        }
        Todo saved = todoRepository.save(todo);
        return TodoMapper.toResponse(saved);
    }

    @Override
    public boolean deleteTodo(String id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException("Todo with id " + id + " not found");
        }
        todoRepository.deleteById(id);
        return true;
    }

    @Override
    public List<TodoResponse> searchTodos(String userId, String search) {
        List<Todo> todos = todoRepository.findByUserIdAndTaskContainingIgnoreCase(userId, search);
        return TodoMapper.toResponseList(todos);
    }
}
