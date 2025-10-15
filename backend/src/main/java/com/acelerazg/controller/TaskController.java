package com.acelerazg.controller;

import com.acelerazg.App;
import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
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
    private final App app;

    public TaskController(App app) {
        this.app = app;
    }

    public Response<Task> createTask(String name, String description, LocalDateTime endDate, int priority, String category, Status status) {
        Response<Void> validation = validateTaskData(name, description, category, status, priority, endDate, true);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());


        Task task = new Task(app.generateNextTaskId(), name, description, endDate, priority, category, status);
        app.getTasks().put(task.getId(), task);
        return Response.success(201, Messages.SUCCESS_TASK_CREATED, task);
    }

    public Response<Task> getTaskById(int id) {
        if (id <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);
        Task task = app.getTasks().get(id);
        return (task == null) ? Response.error(404, Messages.ERROR_NO_TASKS_FOUND) : Response.success(200, Messages.SUCCESS_TASK_RETRIEVED, task);
    }

    public Response<Map<Integer, Task>> getAllTasks() {
        Map<Integer, Task> tasks = app.getTasks().values().stream().sorted().collect(Collectors.toMap(Task::getId, t -> t, (e1, e2) -> e1, LinkedHashMap::new));
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, tasks);
    }

    public Response<Task> updateTask(int id, String name, String description, LocalDateTime endDate, Integer priority, String category, Status status) {

        if (id <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = app.getTasks().get(id);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        String finalName = (name != null && !name.trim().isEmpty()) ? name : task.getName();
        String finalDescription = (description != null && !description.trim().isEmpty()) ? description : task.getDescription();
        String finalCategory = (category != null && !category.trim().isEmpty()) ? category : task.getCategory();

        Status finalStatus = (status != null) ? status : task.getStatus();
        int finalPriority = (priority != null) ? priority : task.getPriority();
        LocalDateTime finalEndDate = (endDate != null) ? endDate : task.getEndDate();

        Response<Void> validation = validateTaskData(finalName, finalDescription, finalCategory, finalStatus, finalPriority, finalEndDate, false);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());

        task.setName(finalName);
        task.setDescription(finalDescription);
        task.setCategory(finalCategory);
        task.setStatus(finalStatus);
        task.setPriority(finalPriority);
        task.setEndDate(finalEndDate);
        task.setModificationDate(LocalDateTime.now());

        return Response.success(200, Messages.SUCCESS_TASK_UPDATED, task);
    }

    public Response<Void> deleteTask(int id) {
        if (id <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);
        if (!app.getTasks().containsKey(id)) {
            return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);
        }
        app.getTasks().remove(id);
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
