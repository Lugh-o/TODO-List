package com.acelerazg.controller;

import com.acelerazg.common.Response;
import com.acelerazg.model.Task;
import com.acelerazg.service.ReminderService;

public class ReminderController {
    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    public Response<Task> createReminder(int taskId, String message, int hoursInAdvance) {
        return reminderService.createReminder(taskId, message, hoursInAdvance);
    }

    public Response<Task> updateReminder(int taskId, int reminderId, String message, Integer hoursInAdvance) {
        return reminderService.updateReminder(taskId, reminderId, message, hoursInAdvance);
    }

    public Response<Void> deleteReminder(int taskId, int reminderId) {
        return reminderService.deleteReminder(taskId, reminderId);
    }
}
