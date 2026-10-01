package com.kpi.io45.bondarchuk.repository;

import com.kpi.io45.bondarchuk.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    // Requirement 5.2: Auto-generated method by Spring Data JPA naming convention
    List<Task> findByPriority(String priority);

    // Requirement 5.2: Another auto-generated method to find completed tasks
    List<Task> findByCompletedTrue();

    // Requirement 5.1.1: Search using JPQL with @Query annotation
    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId")
    List<Task> findTasksByCategoryId(@Param("categoryId") Long categoryId);

    // Requirement 5.1.2: Search using @NamedQuery
    // (The query itself is defined in the Task.java entity class)
    List<Task> findByTitleContaining(@Param("keyword") String keyword);
}