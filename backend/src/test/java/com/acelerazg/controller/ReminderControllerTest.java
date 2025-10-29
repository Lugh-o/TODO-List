package com.acelerazg.controller;

import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.model.Reminder;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;
import com.acelerazg.service.ReminderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderControllerTest {

    @Mock
    private ReminderService reminderService;

    @InjectMocks
    private ReminderController reminderController;

    @Test
    void createReminderSuccessful() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();
        Response<Task> expectedResponse = Response.success(201, Messages.SUCCESS_REMINDER_CREATED, task);
        when(reminderService.createReminder(anyInt(), any(String.class), anyInt())).thenReturn(expectedResponse);

        // WHEN
        Response<Task> response = reminderController.createReminder(1, "Buy milk", 2);

        // THEN
        assertEquals(201, response.getStatusCode());
        assertEquals(Messages.SUCCESS_REMINDER_CREATED, response.getMessage());
        assertEquals(task, response.getData());
        verify(reminderService).createReminder(anyInt(), any(String.class), anyInt());
    }

    @Test
    void updateReminderSuccessful() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();
        Reminder reminder = Reminder.builder().id(1).message("Old message").hoursInAdvance(3).build();
        task.getReminders().put(1, reminder);

        Response<Task> expectedResponse = Response.success(200, Messages.SUCCESS_REMINDER_UPDATED, task);
        when(reminderService.updateReminder(anyInt(), anyInt(), any(String.class), anyInt())).thenReturn(expectedResponse);

        // WHEN
        Response<Task> response = reminderController.updateReminder(1, 1, "Updated message", 5);

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(Messages.SUCCESS_REMINDER_UPDATED, response.getMessage());
    }

    @Test
    void deleteReminderSuccessful() {
        Response<Void> expectedResponse =
                Response.success(204, Messages.SUCCESS_REMINDER_DELETED, null);

        when(reminderService.deleteReminder(anyInt(), anyInt()))
                .thenReturn(expectedResponse);

        Response<Void> response = reminderController.deleteReminder(1, 1);

        assertEquals(204, response.getStatusCode());
        assertEquals(Messages.SUCCESS_REMINDER_DELETED, response.getMessage());
        verify(reminderService).deleteReminder(anyInt(), anyInt());
    }
}
