package com.kpi.io45.bondarchuk.dao;

import com.kpi.io45.bondarchuk.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository("jdbcTemplateDao")
public class TaskDaoJdbcTemplateImpl implements TaskDao {

    private final JdbcTemplate jdbcTemplate;

    public TaskDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        String sql = "INSERT INTO tasks (title, task_date, priority, completed) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDate());
            ps.setString(3, task.getPriority());
            ps.setBoolean(4, task.isCompleted());
            return ps;
        }, keyHolder);

        return ((Number) keyHolder.getKeys().get("id")).longValue();
    }

    @Override
    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        List<Task> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Task> findAll() {
        return jdbcTemplate.query("SELECT * FROM tasks", rowMapper);
    }

    @Override
    public List<Task> findByPriority(String priority) {
        return jdbcTemplate.query("SELECT * FROM tasks WHERE priority = ?", rowMapper, priority);
    }

    @Override
    public void update(Task task) {
        String sql = "UPDATE tasks SET title = ?, task_date = ?, priority = ?, completed = ? WHERE id = ?";
        jdbcTemplate.update(sql, task.getTitle(), task.getDate(), task.getPriority(), task.isCompleted(), task.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
    }
}
