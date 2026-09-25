package com.kpi.io45.bondarchuk.model;
import java.util.UUID;

public class Task {

    private Long id;
    private String title;
    private String date;
    private String priority;
    private boolean completed;

    // Empty constructor for use with Spring JDBC
    public Task() {
    }

    // Constructor for creating a new task
    public Task(String title, String date, String priority) {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.completed = false;
    }

    // Constructor with all fields
    public Task(Long id, String title, String date, String priority, boolean completed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.completed = completed;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
