package com.kpi.io45.bondarchuk.model;
import java.util.UUID;

public class Task {
    private String id;
    private String title;
    private String date;
    private String priority;
    private boolean isCompleted;

    public Task(String title, String date, String priority) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.isCompleted = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { this.isCompleted = completed; }
}
