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
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks API", description = "RESTful web service for task management")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get the list of tasks", description = "Returns a list of tasks with support for status filtering and pagination..")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "\n" + "List successfully retrieved")})
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getTasks(completed, page, size));
    }

    @Operation(summary = "Create a new task")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "The task has been successfully created")})
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        taskService.addTask(task.getTitle(), task.getDate(), task.getPriority());
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Operation(summary = "Get a task by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "\n" + "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Full task update", description = " Updates all fields of the task with the specified ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task task) {
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Partial task update", description = "Updates only the provided task fields (RFC 7386 Merge Patch).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return taskService.patchTask(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted; no content in the response."),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        if (taskService.getTaskById(id).isPresent()) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
