package com.toDoApp.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@lombok.Data
@lombok.AllArgsConstructor
@Document(collection = "todos")
public class Todo {
    @Id
    private String id;

    private String task;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private String userId;

    public Todo() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.completed = false;
    }

    public Todo(String task, LocalDateTime dueDate) {
        this();
        this.task = task;
        this.dueDate = dueDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.updatedAt = LocalDateTime.now();
    }

    public void setTask(String task) {
        this.task = task;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }
}
