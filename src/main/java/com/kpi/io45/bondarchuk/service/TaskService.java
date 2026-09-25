package com.kpi.io45.bondarchuk.service;

import com.kpi.io45.bondarchuk.dao.TaskDao;
import com.kpi.io45.bondarchuk.model.Task;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskDao taskDao;
    private final JdbcClient jdbcClient;

    // We use the implementation via JdbcClient.
    public TaskService(@Qualifier("jdbcClientDao") TaskDao taskDao, JdbcClient jdbcClient) {
        this.taskDao = taskDao;
        this.jdbcClient = jdbcClient;
    }

    public Long addTask(Task task) {
        return taskDao.create(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskDao.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskDao.findAll();
    }

    public List<Task> getTasksByPriority(String priority) {
        return taskDao.findByPriority(priority);
    }

    public void updateTask(Task task) {
        taskDao.update(task);
    }

    public void deleteTask(Long id) {
        taskDao.delete(id);
    }

    // Transaction demonstration
    @Transactional
    public void archiveTask(Long id) {
        // 1. Find task
        Task task = taskDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        // 2. Copy to the archive table.
        jdbcClient.sql("INSERT INTO task_archives (original_task_id, title) VALUES (?, ?)")
                .param(1, task.getId())
                .param(2, task.getTitle())
                .update();


        //if (true) throw new RuntimeException("Artificial error! The transaction should roll back.");

        // 3. Delete from the main table
        taskDao.delete(id);
    }
}