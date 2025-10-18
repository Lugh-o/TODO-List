package com.acelerazg.controller;

import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.dao.TaskDAO;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.acelerazg.common.DataValidator.isNullOrEmpty;
import static com.acelerazg.common.DataValidator.validateTaskData;

public class TaskController {
    private final TaskDAO taskDAO;

    public TaskController(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public Response<Task> createTask(String name, String description, LocalDateTime endDate, int priority, String category, Status status) {
        Response<Void> validation = validateTaskData(name, description, category, status, priority, endDate, true);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());

        Task task = new Task(name, description, endDate, priority, category, status);
        Task createdTask = taskDAO.createTask(task);

        return Response.success(201, Messages.SUCCESS_TASK_CREATED, createdTask);
    }

    public Response<Task> getTaskById(int id) {
        if (id <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = taskDAO.findTaskById(id);
        return (task == null) ? Response.error(404, Messages.ERROR_NO_TASKS_FOUND) : Response.success(200, Messages.SUCCESS_TASK_RETRIEVED, task);
    }

    public Response<Map<Integer, Task>> getAllTasks() {
        Map<Integer, Task> tasks = taskDAO.getTasks().values().stream().sorted().collect(Collectors.toMap(Task::getId, t -> t, (e1, e2) -> e1, LinkedHashMap::new));

        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, tasks);
    }

    public Response<Task> updateTask(int id, String name, String description, LocalDateTime endDate, Integer priority, String category, Status status) {
        if (id <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task existing = taskDAO.findTaskById(id);
        if (existing == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        String finalName = (name != null && !name.trim().isEmpty()) ? name : existing.getName();
        String finalDescription = (description != null && !description.trim().isEmpty()) ? description : existing.getDescription();
        String finalCategory = (category != null && !category.trim().isEmpty()) ? category : existing.getCategory();
        Status finalStatus = (status != null) ? status : existing.getStatus();
        int finalPriority = (priority != null) ? priority : existing.getPriority();
        LocalDateTime finalEndDate = (endDate != null) ? endDate : existing.getEndDate();

        Response<Void> validation = validateTaskData(finalName, finalDescription, finalCategory, finalStatus, finalPriority, finalEndDate, false);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());

        Task updatedData = new Task(finalName, finalDescription, finalEndDate, finalPriority, finalCategory, finalStatus);
        Task updatedTask = taskDAO.updateTask(id, updatedData);

        return Response.success(200, Messages.SUCCESS_TASK_UPDATED, updatedTask);
    }

    public Response<Void> deleteTask(int id) {
        if (id <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = taskDAO.findTaskById(id);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        taskDAO.deleteTask(id);
        return Response.success(204, Messages.SUCCESS_TASK_DELETED, null);
    }

    public Response<Map<Integer, Task>> getAllTasksByPriority(int priority) {
        if (priority < 1 || priority > 5) return Response.error(422, Messages.ERROR_PRIORITY_RANGE);
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, filterTasks(t -> t.getPriority() == priority));
    }

    public Response<Map<Integer, Task>> getAllTasksByStatus(Status status) {
        if (status == null) return Response.error(400, Messages.ERROR_EMPTY_INPUT);
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, filterTasks(t -> t.getStatus() == status));
    }

    public Response<Map<Integer, Task>> getAllTasksByCategory(String category) {
        if (isNullOrEmpty(category)) return Response.error(400, Messages.ERROR_EMPTY_INPUT);
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, filterTasks(t -> t.getCategory().equalsIgnoreCase(category)));
    }

    public Response<Map<Integer, Task>> getTasksByEndDate(LocalDate date) {
        if (date == null) return Response.error(400, Messages.ERROR_EMPTY_INPUT);
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, filterTasks(t -> t.getEndDate() != null && t.getEndDate().toLocalDate().isEqual(date)));
    }

    public Response<Map<Status, Integer>> getStatusCount() {
        Map<Status, Integer> map = Arrays.stream(Status.values()).collect(Collectors.toMap(s -> s, s -> getAllTasksByStatus(s).getData().size()));
        return Response.success(200, Messages.SUCCESS_TASK_COUNT, map);
    }

    private Map<Integer, Task> filterTasks(Predicate<Task> predicate) {
        return getAllTasks().getData().values().stream().filter(predicate).collect(Collectors.toMap(Task::getId, t -> t, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
