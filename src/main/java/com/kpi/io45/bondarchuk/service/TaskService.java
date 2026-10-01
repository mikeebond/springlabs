package com.kpi.io45.bondarchuk.service;

import com.kpi.io45.bondarchuk.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(Task task, Long categoryId);
    Optional<Task> getTaskById(Long id);
    List<Task> getAllTasks();
    List<Task> getTasksByPriority(String priority);
    List<Task> searchTasksByTitle(String keyword);
    Task updateTask(Long id, Task taskDetails);
    void deleteTask(Long id);
    void completeAllTasksInCategory(Long categoryId); // Transactional method requirement
}