package com.acelerazg.controller;

import com.acelerazg.common.Response;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;
import com.acelerazg.service.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public Response<Task> createTask(String name, String description, LocalDateTime endDate, int priority, String category, Status status) {
        return taskService.createTask(name, description, endDate, priority, category, status);
    }

    public Response<Task> getTaskById(int id) {
        return taskService.getTaskById(id);
    }

    public Response<Map<Integer, Task>> getAllTasks() {
        return taskService.getAllTasks();
    }

    public Response<Task> updateTask(int id, String name, String description, LocalDateTime endDate, Integer priority, String category, Status status) {
        return taskService.updateTask(id, name, description, endDate, priority, category, status);
    }

    public Response<Void> deleteTask(int id) {
        return taskService.deleteTask(id);
    }

    public Response<Map<Integer, Task>> getAllTasksByPriority(int priority) {
        return taskService.getAllTasksByPriority(priority);
    }

    public Response<Map<Integer, Task>> getAllTasksByStatus(Status status) {
        return taskService.getAllTasksByStatus(status);
    }

    public Response<Map<Integer, Task>> getAllTasksByCategory(String category) {
        return taskService.getAllTasksByCategory(category);
    }

    public Response<Map<Integer, Task>> getTasksByEndDate(LocalDate date) {
        return taskService.getTasksByEndDate(date);
    }

    public Response<Map<Status, Integer>> getStatusCount() {
        return taskService.getStatusCount();
    }
}
