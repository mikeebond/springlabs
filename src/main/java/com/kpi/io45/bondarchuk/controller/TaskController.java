package com.kpi.io45.bondarchuk.controller;

import com.kpi.io45.bondarchuk.model.Task;
import com.kpi.io45.bondarchuk.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String showTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks";
    }

    @PostMapping("/tasks/add")
    public String addTask(@RequestParam String title, @RequestParam String date, @RequestParam String priority) {
        Task newTask = new Task(title, date, priority);
        taskService.addTask(newTask);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/complete")
    public String completeTask(@RequestParam Long id) {
        // Locate the task using the new Long ID, change the status to `true`, and update the database.
        taskService.getTaskById(id).ifPresent(task -> {
            task.setCompleted(true);
            taskService.updateTask(task);
        });
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/delete")
    public String deleteTask(@RequestParam Long id) {
        // Deleting the task again Long ID
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}