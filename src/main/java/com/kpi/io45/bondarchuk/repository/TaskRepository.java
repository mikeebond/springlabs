package com.kpi.io45.bondarchuk.repository;

import com.kpi.io45.bondarchuk.model.Task;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();

    public TaskRepository() {
        tasks.add(new Task("Зробити лабу №2 зі Spring", "2026-09-17", "Високий"));
        tasks.add(new Task("Підготуватися до пар", "2026-09-18", "Середній"));
    }

    public List<Task> findAll() {
        return tasks;
    }

    public void save(Task task) {
        tasks.add(task);
    }

    public void deleteById(String id) {
        tasks.removeIf(task -> task.getId().equals(id));
    }

    public void markAsCompleted(String id) {
        Optional<Task> taskOpt = tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
        taskOpt.ifPresent(task -> task.setCompleted(true));
    }
}
