package com.acelerazg.controller;

import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.dao.TaskDAO;
import com.acelerazg.model.Reminder;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderControllerTest {

    private ReminderController reminderController;

    @Mock
    private TaskDAO taskDAO;

    @BeforeEach
    void setUp() {
        reminderController = new ReminderController(taskDAO);
    }

    @Test
    void createReminderSuccessful() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();

        when(taskDAO.findTaskById(1)).thenReturn(task);
        when(taskDAO.createReminder(eq(1), any(Reminder.class))).thenAnswer(invocation -> {
            Reminder reminder = invocation.getArgument(1);
            task.getReminders().put(reminder.getId(), reminder);
            return reminder;
        });

        // WHEN
        Response<Task> response = reminderController.createReminder(1, "Buy milk", 2);

        // THEN
        assertEquals(201, response.getStatusCode());
        assertEquals(Messages.SUCCESS_REMINDER_CREATED, response.getMessage());
        assertEquals(1, response.getData().getReminders().size());
        verify(taskDAO).findTaskById(1);
        verify(taskDAO).createReminder(eq(1), any(Reminder.class));
    }

    @Test
    void createReminderInvalidMessage() {
        // GIVEN
        int taskId = 1;
        String message = "";
        int hoursInAdvance = 2;

        // WHEN
        Response<Task> response = reminderController.createReminder(taskId, message, hoursInAdvance);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).createReminder(anyInt(), any());
    }

    @Test
    void createReminderInvalidHours() {
        // GIVEN
        int taskId = 1;
        String message = "Notify me";
        int hoursInAdvance = 0;

        // WHEN
        Response<Task> response = reminderController.createReminder(taskId, message, hoursInAdvance);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).createReminder(anyInt(), any());
    }

    @Test
    void createReminderInvalidTaskId() {
        // GIVEN
        int taskId = -1;

        // WHEN
        Response<Task> response = reminderController.createReminder(taskId, "Message", 2);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).createReminder(anyInt(), any());
    }

    @Test
    void createReminderTaskNotFound() {
        // GIVEN
        when(taskDAO.findTaskById(1)).thenReturn(null);

        // WHEN
        Response<Task> response = reminderController.createReminder(1, "Message", 2);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO).findTaskById(1);
    }

    @Test
    void updateReminderSuccessful() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();

        Reminder reminder = Reminder.builder().id(1).message("Old message").hoursInAdvance(3).build();
        task.getReminders().put(1, reminder);
        when(taskDAO.findTaskById(1)).thenReturn(task);
        when(taskDAO.findReminderById(1, 1)).thenReturn(reminder);
        when(taskDAO.updateReminder(eq(1), eq(1), any(Reminder.class))).thenAnswer(invocation -> {
            Reminder r = invocation.getArgument(2);
            task.getReminders().put(1, r);
            return r;
        });

        // WHEN
        Response<Task> response = reminderController.updateReminder(1, 1, "Updated message", 5);

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(Messages.SUCCESS_REMINDER_UPDATED, response.getMessage());
        Reminder updated = response.getData().getReminders().get(1);
        assertEquals("Updated message", updated.getMessage());
        assertEquals(5, updated.getHoursInAdvance());
        verify(taskDAO).findTaskById(1);
        verify(taskDAO).findReminderById(1, 1);
        verify(taskDAO).updateReminder(eq(1), eq(1), any(Reminder.class));
    }

    @Test
    void updateReminderInvalidIds() {
        // GIVEN
        int taskId = -1;
        int reminderId = -3;

        // WHEN
        Response<Task> response = reminderController.updateReminder(taskId, reminderId, "Message", 2);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).updateReminder(anyInt(), anyInt(), any());
    }

    @Test
    void updateReminderTaskNotFound() {
        // GIVEN
        when(taskDAO.findTaskById(1)).thenReturn(null);

        // WHEN
        Response<Task> response = reminderController.updateReminder(1, 1, "Message", 2);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO).findTaskById(1);
        verify(taskDAO, never()).updateReminder(anyInt(), anyInt(), any());
    }

    @Test
    void updateReminderNotFound() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();

        when(taskDAO.findTaskById(1)).thenReturn(task);

        // WHEN
        Response<Task> response = reminderController.updateReminder(1, 99, "Message", 2);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO).findTaskById(1);
        verify(taskDAO, never()).updateReminder(anyInt(), anyInt(), any());
    }

    @Test
    void updateReminderInvalidData() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();
        Reminder reminder = Reminder.builder().id(1).message("Message").hoursInAdvance(2).build();
        task.getReminders().put(1, reminder);
        when(taskDAO.findTaskById(1)).thenReturn(task);
        when(taskDAO.findReminderById(1, 1)).thenReturn(reminder);

        // WHEN
        Response<Task> response = reminderController.updateReminder(1, 1, "", -3);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO).findTaskById(1);
        verify(taskDAO).findReminderById(1, 1);
        verify(taskDAO, never()).updateReminder(anyInt(), anyInt(), any());
    }

    @Test
    void deleteReminderSuccessful() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();
        Reminder reminder = Reminder.builder().id(1).message("Message").hoursInAdvance(2).build();
        task.getReminders().put(1, reminder);
        when(taskDAO.findTaskById(1)).thenReturn(task);
        doAnswer(invocation -> {
            task.getReminders().remove(1);
            return null;
        }).when(taskDAO).removeReminder(1, 1);

        // WHEN
        Response<Void> response = reminderController.deleteReminder(1, 1);

        // THEN
        assertEquals(204, response.getStatusCode());
        assertEquals(Messages.SUCCESS_REMINDER_DELETED, response.getMessage());
        assertTrue(task.getReminders().isEmpty());
        verify(taskDAO).findTaskById(1);
        verify(taskDAO).removeReminder(1, 1);
    }

    @Test
    void deleteReminderInvalidIds() {
        // GIVEN
        int taskId = -2;
        int reminderId = -1;

        // WHEN
        Response<Void> response = reminderController.deleteReminder(taskId, reminderId);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).removeReminder(anyInt(), anyInt());
    }

    @Test
    void deleteReminderNotFound() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();
        when(taskDAO.findTaskById(1)).thenReturn(task);

        // WHEN
        Response<Void> response = reminderController.deleteReminder(1, 99);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO).findTaskById(1);
        verify(taskDAO, never()).removeReminder(anyInt(), anyInt());
    }
}
