package com.kpi.io45.bondarchuk.service;

import com.kpi.io45.bondarchuk.model.Category;
import com.kpi.io45.bondarchuk.model.Task;
import com.kpi.io45.bondarchuk.repository.CategoryRepository;
import com.kpi.io45.bondarchuk.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskServiceImpl(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Task createTask(Task task, Long categoryId) {
        // Find category by ID, throw exception if not found
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

        task.setCategory(category);
        return taskRepository.save(task); // Spring Data JPA automatically saves the entity
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    @Override
    public List<Task> searchTasksByTitle(String keyword) {
        return taskRepository.findByTitleContaining(keyword);
    }

    @Override
    public Task updateTask(Long id, Task taskDetails) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));

        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDate(taskDetails.getDate());
        existingTask.setPriority(taskDetails.getPriority());
        existingTask.setCompleted(taskDetails.isCompleted());

        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // Requirement 6: At least one transactional method
    @Override
    @Transactional
    public void completeAllTasksInCategory(Long categoryId) {
        // Fetch all tasks for a specific category
        List<Task> tasks = taskRepository.findTasksByCategoryId(categoryId);

        // Update their status
        for (Task task : tasks) {
            task.setCompleted(true);
        }

        // Save all updated tasks in one transaction
        taskRepository.saveAll(tasks);

        // Uncomment the line below later to test transaction rollback in Postman
        // if (true) throw new RuntimeException("Simulated error! Transaction should rollback.");
    }
}