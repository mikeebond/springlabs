package com.kpi.io45.bondarchuk.service;

import com.kpi.io45.bondarchuk.model.Task;
import com.kpi.io45.bondarchuk.repository.TaskRepository;
import com.kpi.io45.bondarchuk.util.DateFormatterHelper;
import com.kpi.io45.bondarchuk.util.PrioritySorterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    // 1. Constructor injection

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 2. Direct field injection
    @Autowired
    private DateFormatterHelper dateFormatter;

    // 3. Setter injection
    private PrioritySorterHelper prioritySorter;

    @Autowired
    public void setPrioritySorter(PrioritySorterHelper prioritySorter) {
        this.prioritySorter = prioritySorter;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void addTask(String title, String date, String priority) {
        taskRepository.save(new Task(title, date, priority));
    }

    public void completeTask(String id) {
        taskRepository.markAsCompleted(id);
    }

    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    // Filtering and pagination
    public List<Task> getTasks(Boolean completed, int page, int size) {
        return taskRepository.findAll().stream()
                .filter(t -> completed == null || t.isCompleted() == completed)
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    // Full update (PUT)
    public Optional<Task> updateTask(String id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDate(updatedTask.getDate());
            task.setPriority(updatedTask.getPriority());
            task.setCompleted(updatedTask.isCompleted());
            return task;
        });
    }

    // Partial update - RFC 7386 Merge Patch (PATCH)
    public Optional<Task> patchTask(String id, Map<String, Object> updates) {
        return taskRepository.findById(id).map(task -> {
            if (updates.containsKey("title")) task.setTitle((String) updates.get("title"));
            if (updates.containsKey("date")) task.setDate((String) updates.get("date"));
            if (updates.containsKey("priority")) task.setPriority((String) updates.get("priority"));
            if (updates.containsKey("completed")) task.setCompleted((Boolean) updates.get("completed"));
            return task;
        });
    }
}