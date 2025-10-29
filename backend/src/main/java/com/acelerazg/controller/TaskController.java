package com.acelerazg.controller;

import com.acelerazg.common.Response;
import com.acelerazg.dto.CreateTaskDTO;
import com.acelerazg.dto.UpdateTaskDTO;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;
import com.acelerazg.service.TaskService;

import java.time.LocalDate;
import java.util.Map;

public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public Response<Task> createTask(CreateTaskDTO createTaskDTO) {
        return taskService.createTask(createTaskDTO);
    }

    public Response<Task> getTaskById(int id) {
        return taskService.getTaskById(id);
    }

    public Response<Map<Integer, Task>> getAllTasks() {
        return taskService.getAllTasks();
    }

    public Response<Task> updateTask(UpdateTaskDTO updateTaskDTO) {
        return taskService.updateTask(updateTaskDTO);
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
