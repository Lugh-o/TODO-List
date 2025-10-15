package com.acelerazg.controller;

import com.acelerazg.App;
import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.model.Reminder;
import com.acelerazg.model.Task;

import static com.acelerazg.common.DataValidator.validateReminderData;

public class ReminderController {
    private final App app;

    public ReminderController(App app) {
        this.app = app;
    }

    public Response<Task> createReminder(int taskId, String message, int hoursInAdvance) {
        Response<Void> validation = validateReminderData(message, hoursInAdvance);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());
        if (taskId <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = app.getTasks().get(taskId);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        Reminder reminder = new Reminder(app.generateNextReminderId(), message, hoursInAdvance);
        task.getReminders().put(reminder.getId(), reminder);

        return Response.success(201, Messages.SUCCESS_REMINDER_CREATED, task);
    }

    public Response<Task> updateReminder(int taskId, int reminderId, String message, Integer hoursInAdvance) {
        if (taskId <= 0 || reminderId <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = app.getTasks().get(taskId);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        Reminder reminder = task.getReminders().get(reminderId);
        if (reminder == null) return Response.error(404, Messages.ERROR_REMINDER_NOT_FOUND);

        String finalMessage = (message != null && !message.trim().isEmpty()) ? message : reminder.getMessage();

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
        Task task = app.getTasks().get(taskId);
        if (task == null || !task.getReminders().containsKey(reminderId)) {
            return Response.error(404, Messages.ERROR_REMINDER_NOT_FOUND);
        }
        task.getReminders().remove(reminderId);
        return Response.success(204, Messages.SUCCESS_TASK_DELETED, null);
    }

}
