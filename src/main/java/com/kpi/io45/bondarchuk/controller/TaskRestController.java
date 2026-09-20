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
@Tag(name = "Tasks API", description = "RESTful вебсервіс для управління задачами")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Отримати список задач", description = "Повертає список задач із підтримкою фільтрації за статусом та пагінації.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успішне отримання списку")})
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getTasks(completed, page, size));
    }

    @Operation(summary = "Створити нову задачу")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Задачу успішно створено")})
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        taskService.addTask(task.getTitle(), task.getDate(), task.getPriority());
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Operation(summary = "Отримати задачу за ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задачу знайдено"),
            @ApiResponse(responseCode = "404", description = "Задачу не знайдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Повне оновлення задачі", description = "Оновлює всі поля задачі за вказаним ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успішно оновлено"),
            @ApiResponse(responseCode = "404", description = "Задачу не знайдено")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task task) {
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Часткове оновлення задачі", description = "Оновлює лише передані поля задачі (RFC 7386 Merge Patch).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успішно оновлено"),
            @ApiResponse(responseCode = "404", description = "Задачу не знайдено")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return taskService.patchTask(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Видалити задачу")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Успішно видалено без контенту у відповіді"),
            @ApiResponse(responseCode = "404", description = "Задачу не знайдено")
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
