package com.acelerazg.controller;

import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.dao.TaskDAO;
import com.acelerazg.model.Reminder;
import com.acelerazg.model.Task;

import static com.acelerazg.common.DataValidator.validateReminderData;

public class ReminderController {
    private final TaskDAO taskDAO;

    public ReminderController(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public Response<Task> createReminder(int taskId, String message, int hoursInAdvance) {
        if (taskId <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Response<Void> validation = validateReminderData(message, hoursInAdvance);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());

        Task task = taskDAO.findTaskById(taskId);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        Reminder reminder = new Reminder(message, hoursInAdvance);
        taskDAO.createReminder(taskId, reminder);

        return Response.success(201, Messages.SUCCESS_REMINDER_CREATED, task);
    }

    public Response<Task> updateReminder(int taskId, int reminderId, String message, Integer hoursInAdvance) {
        if (taskId <= 0 || reminderId <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = taskDAO.findTaskById(taskId);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        Reminder reminder = taskDAO.findReminderById(taskId, reminderId);
        if (reminder == null) return Response.error(404, Messages.ERROR_REMINDER_NOT_FOUND);

        String finalMessage = (message != null && !message.trim().isEmpty()) ? message : reminder.getMessage();
        int finalHours = (hoursInAdvance != null) ? hoursInAdvance : reminder.getHoursInAdvance();

        Response<Void> validation = validateReminderData(finalMessage, finalHours);
        if (validation.getStatusCode() != 200)
            return Response.error(validation.getStatusCode(), validation.getMessage());

        Reminder newReminder = new Reminder(finalMessage, finalHours);
        taskDAO.updateReminder(taskId, reminderId, newReminder);

        return Response.success(200, Messages.SUCCESS_REMINDER_UPDATED, task);
    }

    public Response<Void> deleteReminder(int taskId, int reminderId) {
        if (taskId <= 0 || reminderId <= 0) return Response.error(400, Messages.ERROR_INVALID_ID);

        Task task = taskDAO.findTaskById(taskId);
        if (task == null) return Response.error(404, Messages.ERROR_TASK_NOT_FOUND);

        if (!task.getReminders().containsKey(reminderId)) return Response.error(404, Messages.ERROR_REMINDER_NOT_FOUND);

        taskDAO.removeReminder(taskId, reminderId);
        return Response.success(204, Messages.SUCCESS_REMINDER_DELETED, null);
    }
}
