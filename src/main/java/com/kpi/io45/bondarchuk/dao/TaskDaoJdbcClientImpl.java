package com.kpi.io45.bondarchuk.dao;

import com.kpi.io45.bondarchuk.model.Task;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbcClientDao")
public class TaskDaoJdbcClientImpl implements TaskDao {

    private final JdbcClient jdbcClient;

    public TaskDaoJdbcClientImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private final RowMapper<Task> rowMapper = (rs, rowNum) -> new Task(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("task_date"),
            rs.getString("priority"),
            rs.getBoolean("completed")
    );

    @Override
    public Long create(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO tasks (title, task_date, priority, completed) VALUES (?, ?, ?, ?)")
                .param(1, task.getTitle())
                .param(2, java.time.LocalDate.parse(task.getDate()))
                .param(3, task.getPriority())
                .param(4, task.isCompleted())
                .update(keyHolder);
        return ((Number) keyHolder.getKeys().get("id")).longValue();
    }

    @Override
    public Optional<Task> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM tasks WHERE id = ?")
                .param(1, id)
                .query(rowMapper)
                .optional();
    }

    @Override
    public List<Task> findAll() {
        return jdbcClient.sql("SELECT * FROM tasks")
                .query(rowMapper)
                .list();
    }

    @Override
    public List<Task> findByPriority(String priority) {
        return jdbcClient.sql("SELECT * FROM tasks WHERE priority = ?")
                .param(1, priority)
                .query(rowMapper)
                .list();
    }

    @Override
    public void update(Task task) {
        jdbcClient.sql("UPDATE tasks SET title = ?, task_date = ?, priority = ?, completed = ? WHERE id = ?")
                .param(1, task.getTitle())
                .param(2, java.time.LocalDate.parse(task.getDate()))
                .param(3, task.getPriority())
                .param(4, task.isCompleted())
                .param(5, task.getId())
                .update();
    }

    @Override
    public void delete(Long id) {
        jdbcClient.sql("DELETE FROM tasks WHERE id = ?")
                .param(1, id)
                .update();
    }
}