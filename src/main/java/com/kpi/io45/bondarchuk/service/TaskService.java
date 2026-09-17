package com.kpi.io45.bondarchuk.service;

import com.kpi.io45.bondarchuk.model.Task;
import com.kpi.io45.bondarchuk.repository.TaskRepository;
import com.kpi.io45.bondarchuk.util.DateFormatterHelper;
import com.kpi.io45.bondarchuk.util.PrioritySorterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
// Твої імпорти Task, TaskRepository, DateFormatterHelper, PrioritySorterHelper

@Service
public class TaskService {

    // 1. Ін'єкція через конструктор
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 2. Ін'єкція напряму в поле
    @Autowired
    private DateFormatterHelper dateFormatter;

    // 3. Ін'єкція через сетер
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
}