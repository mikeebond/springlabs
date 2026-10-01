package com.kpi.io45.bondarchuk.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
// Implementation of item 5.1.2: Search using @NamedQuery
@NamedQuery(
        name = "Task.findByTitleContaining",
        query = "SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))"
)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-generation by the database (SERIAL)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "task_date")
    private LocalDate date;

    private String priority;

    @Column(name = "completed")
    private boolean completed;

    //N:1 relationship (Many tasks belong to one category)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    //A no-argument constructor is required for Hibernate to work.
    public Task() {
    }

    // Constructor for creating a new task
    public Task(String title, LocalDate date, String priority, Category category) {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.completed = false;
        this.category = category;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}