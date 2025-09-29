package com.toDoApp.service.todo;

import com.toDoApp.data.repository.TodoRepository;
import com.toDoApp.dto.request.TodoRequest;
import com.toDoApp.dto.response.TodoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    public void testCreateAndFetchTodo() {
        TodoRequest request = TodoRequest.builder()
                .task("Write test")
                .completed(false)
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();

        TodoResponse created = todoService.createTodo("1", request);

        assertNotNull(created.getId());
        assertEquals("Write test", created.getTask());
        assertEquals("1", created.getUserId());

        List<TodoResponse> todos = todoService.getTodosByUser("1");
        assertEquals(1, todos.size());
        assertEquals("Write test", todos.get(0).getTask());
    }

    @Test
    public void testUpdateTodo() {
        TodoRequest request = TodoRequest.builder()
                .task("Initial task")
                .completed(false)
                .dueDate(LocalDateTime.now().plusDays(2))
                .build();

        TodoResponse created = todoService.createTodo("1", request);

        TodoRequest updateRequest = TodoRequest.builder()
                .task("Updated task")
                .completed(true)
                .dueDate(LocalDateTime.now().plusDays(3))
                .build();

        TodoResponse updated = todoService.updateTodo(created.getId(), updateRequest);

        assertEquals("Updated task", updated.getTask());
        assertTrue(updated.isCompleted());
    }

    @Test
    public void testUpdateTodoStatus() {
        TodoRequest request = TodoRequest.builder()
                .task("Status change task")
                .completed(false)
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();
        TodoResponse created = todoService.createTodo("1", request);
        TodoResponse updated = todoService.updateTodoStatus(created.getId(), true);
        assertTrue(updated.isCompleted());
    }
    @Test
    public void testSearchTodos() {
        todoService.createTodo("1", TodoRequest.builder()
                .task("Buy milk")
                .completed(false)
                .dueDate(LocalDateTime.now().plusDays(1))
                .build());
        todoService.createTodo("1", TodoRequest.builder()
                .task("Write code")
                .completed(false)
                .dueDate(LocalDateTime.now().plusDays(1))
                .build());
        List<TodoResponse> searchResults = todoService.searchTodos("1", "milk");
        assertEquals(1, searchResults.size());
        assertEquals("Buy milk", searchResults.get(0).getTask());
    }
    @Test
    public void testDeleteTodo() {
        TodoResponse created = todoService.createTodo("1", TodoRequest.builder()
                .task("Delete me")
                .completed(false)
                .dueDate(LocalDateTime.now().plusDays(1))
                .build());
        boolean deleted = todoService.deleteTodo(created.getId());
        assertTrue(deleted);
        List<TodoResponse> todos = todoService.getTodosByUser("1");
        assertTrue(todos.isEmpty());
    }
}
