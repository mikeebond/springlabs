package com.kpi.io45.bondarchuk.controller;

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
        taskService.addTask(title, date, priority);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/complete")
    public String completeTask(@RequestParam String id) {
        taskService.completeTask(id);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/delete")
    public String deleteTask(@RequestParam String id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}