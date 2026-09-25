package com.kpi.io45.bondarchuk.controller;

import com.kpi.io45.bondarchuk.model.Task;
import com.kpi.io45.bondarchuk.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks API (JDBC)", description = "RESTful web service for task management with a PostgreSQL connection")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get a list of all tasks")
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) String priority) {
        if (priority != null) {
            return ResponseEntity.ok(taskService.getTasksByPriority(priority));
        }
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(summary = "Create a new task")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "The task has been successfully created.")})
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Long generatedId = taskService.addTask(task);
        task.setId(generatedId);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Operation(summary = "Get a task by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "\n" + "Full task update")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @RequestBody Task task) {
        if (taskService.getTaskById(id).isEmpty()) return ResponseEntity.notFound().build();
        task.setId(id);
        taskService.updateTask(task);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete task")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.getTaskById(id).isEmpty()) return ResponseEntity.notFound().build();
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "\n" + "Move the task to the archive", description = "Copies the task to the archive table and deletes it from the main table.")
    @PostMapping("/{id}/archive")
    public ResponseEntity<Void> archiveTask(@PathVariable Long id) {
        try {
            taskService.archiveTask(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}