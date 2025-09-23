package com.toDoApp.data.repository;

import com.toDoApp.data.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
    List<Todo> findByUserId(String userId);
    List<Todo> findByUserIdAndCompleted(String userId, boolean completed);
    List<Todo> findByUserIdAndTaskContainingIgnoreCase(String userId, String task);
}
