package com.acelerazg.todolist;

import com.acelerazg.todolist.common.Messages;
import com.acelerazg.todolist.common.Response;
import com.acelerazg.todolist.persistency.CsvUtilities;
import com.acelerazg.todolist.task.Status;
import com.acelerazg.todolist.task.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TodoList {

    private Map<Integer, Task> tasks = new LinkedHashMap<>();
    private int nextId = 1;

    public Response<Task> createTask(String name, String description, LocalDateTime endDate,
                                     int priority, String category, Status status) {

        Response<Void> validationError = validateTaskData(name, description, category, status, priority, endDate, true);
        if (validationError.getStatusCode() != 200) {
            return Response.error(validationError.getStatusCode(), validationError.getMessage());
        }

        Task task = new Task(nextId++, name, description, endDate, priority, category, status);
        tasks.put(task.getId(), task);

        return Response.success(201, Messages.SUCCESS_TASK_CREATED, task);
    }

    public Response<Task> getTaskById(int id) {
        if (id <= 0) {
            return Response.error(400, Messages.ERROR_INVALID_ID);
        }
        Task task = tasks.get(id);
        if (task == null) {
            return Response.error(404, Messages.ERROR_NO_TASKS_FOUND);
        }
        return Response.success(200, Messages.SUCCESS_TASK_RETRIEVED, task);
    }

    public Response<Map<Integer, Task>> getAllTasks() {
        List<Task> sortedTasks = new ArrayList<>(tasks.values());
        Collections.sort(sortedTasks);
        Map<Integer, Task> sortedMap = new LinkedHashMap<>();
        for (Task task : sortedTasks) {
            sortedMap.put(task.getId(), task);
        }
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, sortedMap);
    }

    public Response<Task> updateTask(int id, String name, String description, LocalDateTime endDate,
                                     Integer priority, String category, Status status) {

        if (id <= 0) {
            return Response.error(400, Messages.ERROR_INVALID_ID);
        }
        Task existing = tasks.get(id);
        if (existing == null) {
            return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);
        }

        String finalName = (name != null && !name.trim().isEmpty()) ? name : existing.getName();
        String finalDescription = (description != null && !description.trim().isEmpty()) ? description : existing.getDescription();
        String finalCategory = (category != null && !category.trim().isEmpty()) ? category : existing.getCategory();
        Status finalStatus = (status != null) ? status : existing.getStatus();
        int finalPriority = (priority != null) ? priority : existing.getPriority();
        LocalDateTime finalEndDate = (endDate != null) ? endDate : existing.getEndDate();

        Response<Void> validationError = validateTaskData(finalName, finalDescription, finalCategory, finalStatus, finalPriority, finalEndDate, false);
        if (validationError.getStatusCode() != 200) {
            return Response.error(validationError.getStatusCode(), validationError.getMessage());
        }

        existing.setName(finalName);
        existing.setDescription(finalDescription);
        existing.setCategory(finalCategory);
        existing.setStatus(finalStatus);
        existing.setPriority(finalPriority);
        existing.setEndDate(finalEndDate);
        existing.setModificationDate(LocalDateTime.now());

        return Response.success(200, Messages.SUCCESS_TASK_UPDATED, existing);
    }

    public Response<Void> deleteTask(int id) {
        if (id <= 0) {
            return Response.error(400, Messages.ERROR_INVALID_ID);
        }
        if (!tasks.containsKey(id)) {
            return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);
        }
        tasks.remove(id);
        return Response.success(204, Messages.SUCCESS_TASK_DELETED, null);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public Response<Map<Integer, Task>> getAllTasksByPriority(int priority) {
        if (priority < 1 || priority > 5) {
            return Response.error(422, Messages.ERROR_PRIORITY_RANGE);
        }
        Map<Integer, Task> filteredTasks = filterTasks(task -> task.getPriority() == priority);
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, filteredTasks);
    }

    public Response<Map<Integer, Task>> getAllTasksByStatus(Status status) {
        if (status == null) {
            return Response.error(400, Messages.ERROR_EMPTY_INPUT);
        }
        Map<Integer, Task> filteredTasks = filterTasks(task -> task.getStatus() == status);
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, filteredTasks);
    }

    public Response<Map<Integer, Task>> getAllTasksByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return Response.error(400, Messages.ERROR_EMPTY_INPUT);
        }
        Map<Integer, Task> filteredTasks = filterTasks(task -> task.getCategory().equalsIgnoreCase(category));
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, filteredTasks);
    }

    public Response<Map<Integer, Task>> getTasksByEndDate(LocalDate date) {
        if (date == null) {
            return Response.error(400, Messages.ERROR_EMPTY_INPUT);
        }
        Map<Integer, Task> filteredTasks = filterTasks(
                task -> task.getEndDate() != null && task.getEndDate().toLocalDate().isEqual(date)
        );
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, filteredTasks);
    }

    public Response<Map<Status, Integer>> getStatusCount(){
        Map<Status, Integer> map = new HashMap<>();
        map.put(Status.TODO, getAllTasksByStatus(Status.TODO).getData().size());
        map.put(Status.DOING, getAllTasksByStatus(Status.DOING).getData().size());
        map.put(Status.DONE, getAllTasksByStatus(Status.DONE).getData().size());
        return Response.success(200,Messages.SUCCESS_TASK_COUNT, map);
    }

    public Response<Map<Status, Integer>> saveDataToCsv(){
        try{
            CsvUtilities.saveTasksToCsv(tasks, "tasks.csv");
            return Response.success(200, Messages.SUCCESS_SAVE_DATA, null);
        } catch (IOException e) {
            return Response.error(500, Messages.ERROR_SAVE_DATA);
        }
    }

    public Response<Map<Status, Integer>> loadDataFromCsv(){
        try {
            this.tasks = CsvUtilities.loadTasksFromCsv("tasks.csv");
            return Response.success(200, Messages.SUCCESS_LOAD_DATA, null);
        } catch (IOException e) {
            return Response.error(500, Messages.ERROR_LOAD_DATA);
        }
    }

    private Map<Integer, Task> filterTasks(Predicate<Task> predicate) {
        return getAllTasks().getData().entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Response<Void> validateTaskData(String name, String description, String category, Status status,
                                            int priority, LocalDateTime endDate, boolean fullValidation) {

        if (fullValidation) {
            if (isNullOrEmpty(name) || isNullOrEmpty(description) || isNullOrEmpty(category) || status == null) {
                return Response.error(400, Messages.ERROR_INVALID_INPUT);
            }
        }

        if (isNullOrEmpty(name)) {
            return Response.error(400, Messages.ERROR_EMPTY_NAME);
        }

        if (priority < 1 || priority > 5) {
            return Response.error(422, Messages.ERROR_PRIORITY_RANGE);
        }

        if (endDate != null && endDate.isBefore(LocalDateTime.now())) {
            return Response.error(422, Messages.ERROR_END_DATE_PAST);
        }

        return Response.success(200, Messages.SUCCESS_VALIDATION_PASSED, null);
    }
}