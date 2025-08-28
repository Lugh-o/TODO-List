package com.acelerazg.todolist;

import com.acelerazg.todolist.common.Messages;
import com.acelerazg.todolist.common.Response;
import com.acelerazg.todolist.persistency.XmlData;
import com.acelerazg.todolist.persistency.XmlUtilities;
import com.acelerazg.todolist.task.Reminder;
import com.acelerazg.todolist.task.Status;
import com.acelerazg.todolist.task.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TodoList {

    private Map<Integer, Task> tasks = new HashMap<>();
    private int nextTaskId = 1;
    private int nextReminderId = 1;

    public Response<Task> createTask(String name, String description, LocalDateTime endDate,
                                     int priority, String category, Status status) {

        Response<Void> validation = validateTaskData(name, description, category, status, priority, endDate, true);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());

        Task task = new Task(nextTaskId++, name, description, endDate, priority, category, status);
        tasks.put(task.getId(), task);
        return Response.success(201, Messages.SUCCESS_TASK_CREATED, task);
    }

    public Response<Task> getTaskById(int id) {
        if (id <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);
        Task task = tasks.get(id);
        return (task == null)
                ? Response.error(404, Messages.ERROR_NO_TASKS_FOUND)
                : Response.success(200, Messages.SUCCESS_TASK_RETRIEVED, task);
    }

    public Response<Map<Integer, Task>> getAllTasks() {
        return Response.success(
                200,
                Messages.SUCCESS_TASKS_RETRIEVED,
                tasks.values().stream()
                        .sorted()
                        .collect(Collectors.toMap(Task::getId, t -> t, (e1, e2) -> e1, LinkedHashMap::new))
        );
    }

    public Response<Task> updateTask(int id, String name, String description, LocalDateTime endDate,
                                     Integer priority, String category, Status status) {

        if (id <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = tasks.get(id);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        String finalName = firstNonEmpty(name, task.getName());
        String finalDescription = firstNonEmpty(description, task.getDescription());
        String finalCategory = firstNonEmpty(category, task.getCategory());
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
        if (!tasks.containsKey(id)) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);
        tasks.remove(id);
        return Response.success(204, Messages.SUCCESS_TASK_DELETED, null);
    }

    public Response<Task> createReminder(int taskId, String message, int hoursInAdvance) {
        Response<Void> validation = validateReminderData(message, hoursInAdvance);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());
        if (taskId <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = tasks.get(taskId);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        Reminder reminder = new Reminder(nextReminderId++, message, hoursInAdvance);
        task.getReminders().put(reminder.getId(), reminder);

        return Response.success(201, Messages.SUCCESS_REMINDER_CREATED, task);
    }

    public Response<Task> updateReminder(int taskId, int reminderId, String message, Integer hoursInAdvance) {
        if (taskId <= 0 || reminderId <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = tasks.get(taskId);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        Reminder reminder = task.getReminders().get(reminderId);
        if (reminder == null) return Response.error(404, Messages.ERROR_REMINDER_NOT_FOUND);

        String finalMessage = firstNonEmpty(message, reminder.getMessage());
        int finalHours = (hoursInAdvance != null) ? hoursInAdvance : reminder.getHoursInAdvance();

        Response<Void> validation = validateReminderData(finalMessage, finalHours);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());

        reminder.setMessage(finalMessage);
        reminder.setHoursInAdvance(finalHours);

        return Response.success(200, Messages.SUCCESS_REMINDER_UPDATED, task);
    }

    public Response<Void> deleteReminder(int taskId, int reminderId) {
        if (taskId <= 0 || reminderId <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);
        Task task = tasks.get(taskId);
        if (task == null || !task.getReminders().containsKey(reminderId)) {
            return Response.error(404, Messages.ERROR_REMINDER_NOT_FOUND);
        }
        task.getReminders().remove(reminderId);
        return Response.success(204, Messages.SUCCESS_TASK_DELETED, null);
    }

    private String firstNonEmpty(String value, String fallback) {
        return (value != null && !value.trim().isEmpty()) ? value : fallback;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
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
        return Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED,
                filterTasks(t -> t.getEndDate() != null && t.getEndDate().toLocalDate().isEqual(date)));
    }

    public Response<Map<Status, Integer>> getStatusCount() {
        Map<Status, Integer> map = Arrays.stream(Status.values())
                .collect(Collectors.toMap(s -> s, s -> getAllTasksByStatus(s).getData().size()));
        return Response.success(200, Messages.SUCCESS_TASK_COUNT, map);
    }

    public Response<Void> saveDataToXml(String filePath) {
        try {
            XmlUtilities.saveTasksToXml(tasks, nextTaskId, nextReminderId, filePath);
            return Response.success(200, Messages.SUCCESS_SAVE_DATA, null);
        } catch (Exception e) {
            return Response.error(500, Messages.ERROR_SAVE_DATA);
        }
    }

    public Response<Void> loadDataFromXml(String filePath) {
        try {
            XmlData data = XmlUtilities.loadTasksFromXml(filePath);
            this.tasks = data.getTasks();
            this.nextTaskId = data.getNextTaskId();
            this.nextReminderId = data.getNextReminderId();
            return Response.success(200, Messages.SUCCESS_LOAD_DATA, null);
        } catch (Exception e) {
            return Response.error(500, Messages.ERROR_LOAD_DATA);
        }
    }

    private Map<Integer, Task> filterTasks(Predicate<Task> predicate) {
        return getAllTasks().getData().values().stream()
                .filter(predicate)
                .collect(Collectors.toMap(Task::getId, t -> t, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Response<Void> validateTaskData(String name, String description, String category, Status status,
                                            int priority, LocalDateTime endDate, boolean fullValidation) {
        if (fullValidation && (isNullOrEmpty(name) || isNullOrEmpty(description) || isNullOrEmpty(category) || status == null)) {
            return Response.error(400, Messages.ERROR_INVALID_INPUT);
        }
        if (isNullOrEmpty(name)) return Response.error(400, Messages.ERROR_EMPTY_NAME);
        if (priority < 1 || priority > 5) return Response.error(422, Messages.ERROR_PRIORITY_RANGE);
        if (endDate != null && endDate.isBefore(LocalDateTime.now()))
            return Response.error(422, Messages.ERROR_END_DATE_PAST);
        return Response.success(200, Messages.SUCCESS_VALIDATION_PASSED, null);
    }

    private Response<Void> validateReminderData(String message, int hoursInAdvance) {
        if (isNullOrEmpty(message)) return Response.error(400, Messages.ERROR_REMINDER_EMPTY_MESSAGE);
        if (hoursInAdvance <= 0) return Response.error(422, Messages.ERROR_REMINDER_HOURS_RANGE);
        return Response.success(200, Messages.SUCCESS_VALIDATION_PASSED, null);
    }
}
