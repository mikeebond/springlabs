package com.kpi.io45.bondarchuk.dao;

import com.kpi.io45.bondarchuk.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskDao {
    Long create(Task task); // Returns the generated ID
    Optional<Task> findById(Long id);
    List<Task> findAll();
    List<Task> findByPriority(String priority); //Search by selected criterion
    void update(Task task);
    void delete(Long id);
}
