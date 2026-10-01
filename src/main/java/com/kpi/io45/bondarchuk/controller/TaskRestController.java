package com.kpi.io45.bondarchuk.controller;

import com.kpi.io45.bondarchuk.model.Task;
import com.kpi.io45.bondarchuk.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks API", description = "RESTful API for Task Management using Spring Data JPA")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Get all tasks",
            description = "Retrieves a list of all tasks. Can be filtered by priority or a keyword in the title."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of tasks")
    })
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @Parameter(description = "Filter by task priority") @RequestParam(required = false) String priority,
            @Parameter(description = "Search keyword in task title") @RequestParam(required = false) String keyword) {

        if (priority != null) {
            return ResponseEntity.ok(taskService.getTasksByPriority(priority));
        } else if (keyword != null) {
            return ResponseEntity.ok(taskService.searchTasksByTitle(keyword));
        }
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task and assigns it to a specific category."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task successfully created",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or missing category", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Task> createTask(
            @Parameter(description = "Task object containing details") @RequestBody Task task,
            @Parameter(description = "ID of the category to assign the task to", required = true) @RequestParam Long categoryId) {
        try {
            Task createdTask = taskService.createTask(task, categoryId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get task by ID", description = "Fetches a single task by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "Unique ID of the task") @PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an existing task", description = "Updates task details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task successfully updated"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "ID of the task to update") @PathVariable Long id,
            @Parameter(description = "Updated task details") @RequestBody Task taskDetails) {
        try {
            return ResponseEntity.ok(taskService.updateTask(id, taskDetails));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a task", description = "Removes a task from the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete") @PathVariable Long id) {
        if (taskService.getTaskById(id).isEmpty()) return ResponseEntity.notFound().build();
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Complete all tasks in a category (Transactional)",
            description = "Marks all tasks belonging to a specific category as completed. Uses @Transactional to ensure data integrity."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tasks successfully updated"),
            @ApiResponse(responseCode = "500", description = "Internal server error (if transaction fails)")
    })
    @PostMapping("/categories/{categoryId}/complete")
    public ResponseEntity<Void> completeAllTasksInCategory(
            @Parameter(description = "ID of the category") @PathVariable Long categoryId) {
        taskService.completeAllTasksInCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}