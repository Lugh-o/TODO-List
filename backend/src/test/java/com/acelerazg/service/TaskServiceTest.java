package com.acelerazg.service;

import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.dao.TaskDAO;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskDAO taskDAO;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTaskSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Task createdTask = Task.builder().id(1).name("Task 1").description("Description").endDate(endDate).priority(4).category("Work").status(Status.TODO).build();
        when(taskDAO.createTask(any(Task.class))).thenReturn(createdTask);

        // WHEN
        Response<Task> response = taskService.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // THEN
        assertEquals(201, response.getStatusCode());
        assertEquals(Messages.SUCCESS_TASK_CREATED, response.getMessage());
        assertEquals(createdTask, response.getData());
        verify(taskDAO).createTask(any(Task.class));
    }

    @Test
    void createTaskEmptyName() {
        // GIVEN
        String name = "";

        // WHEN
        Response<Task> response = taskService.createTask(name, "Description", LocalDateTime.now().plusDays(1), 3, "Work", Status.TODO);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).createTask(any());
    }

    @Test
    void createTaskPriorityOutOfRange() {
        // GIVEN
        int priority = 20;

        // WHEN
        Response<Task> response = taskService.createTask("Task", "Description", LocalDateTime.now().plusDays(1), priority, "Work", Status.TODO);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).createTask(any());
    }

    @Test
    void createTaskPastEndDate() {
        // GIVEN
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        // WHEN
        Response<Task> response = taskService.createTask("Task", "Description", pastDate, 3, "Work", Status.TODO);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).createTask(any());
    }

    @Test
    void getTaskByIdSuccessful() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(4).category("Work").status(Status.TODO).build();
        when(taskDAO.findTaskById(1)).thenReturn(task);

        // WHEN
        Response<Task> response = taskService.getTaskById(1);

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(task, response.getData());
        verify(taskDAO).findTaskById(1);
    }

    @Test
    void getTaskByIdInvalid() {
        // GIVEN
        int taskId = -1;

        // WHEN
        Response<Task> response = taskService.getTaskById(taskId);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).findTaskById(anyInt());
    }

    @Test
    void getTaskByIdNoTask() {
        // GIVEN
        when(taskDAO.findTaskById(1)).thenReturn(null);

        // WHEN
        Response<Task> response = taskService.getTaskById(1);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO).findTaskById(1);
    }

    @Test
    void getAllTasks() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();
        Map<Integer, Task> tasks = new HashMap<>();
        tasks.put(1, task);
        when(taskDAO.getTasks()).thenReturn(tasks);

        // WHEN
        Response<Map<Integer, Task>> response = taskService.getAllTasks();

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(tasks, response.getData());
        verify(taskDAO).getTasks();
    }

    @Test
    void updateTaskSuccessful() {
        // GIVEN
        Task existing = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(4).category("Work").status(Status.TODO).build();
        Task updated = Task.builder().id(1).name("Updated").description("Description").endDate(existing.getEndDate()).priority(4).category("Work").status(Status.TODO).build();
        when(taskDAO.findTaskById(1)).thenReturn(existing);
        when(taskDAO.updateTask(eq(1), any(Task.class))).thenReturn(updated);

        // WHEN
        Response<Task> response = taskService.updateTask(1, "Updated", null, null, null, null, Status.DONE);

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(updated, response.getData());
        verify(taskDAO).updateTask(eq(1), any(Task.class));
    }

    @Test
    void updateTaskInvalidId() {
        // GIVEN
        int taskId = -13;

        // WHEN
        Response<Task> response = taskService.updateTask(taskId, "Task Updated", null, null, null, null, Status.DONE);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).updateTask(anyInt(), any());
    }

    @Test
    void updateTaskNoTask() {
        // GIVEN
        when(taskDAO.findTaskById(2)).thenReturn(null);

        // WHEN
        Response<Task> response = taskService.updateTask(2, "Task Updated", null, null, null, null, Status.DONE);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO).findTaskById(2);
        verify(taskDAO, never()).updateTask(anyInt(), any());
    }

    @Test
    void updateTaskPriorityOutOfRange() {
        // GIVEN
        Task existingTask = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(4).category("Work").status(Status.TODO).build();
        when(taskDAO.findTaskById(1)).thenReturn(existingTask);
        int invalidPriority = 30;

        // WHEN
        Response<Task> response = taskService.updateTask(1, "Task Updated", null, null, invalidPriority, null, Status.DONE);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).updateTask(anyInt(), any());
    }

    @Test
    void updateTaskPastEndDate() {
        // GIVEN
        Task existingTask = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(3).category("Work").status(Status.TODO).build();
        when(taskDAO.findTaskById(1)).thenReturn(existingTask);
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        // WHEN
        Response<Task> response = taskService.updateTask(1, "Task Updated", null, pastDate, 3, null, Status.DONE);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).updateTask(anyInt(), any());
    }

    @Test
    void deleteTaskSuccessful() {
        // GIVEN
        Task existing = Task.builder().id(1).name("Task 1").description("Description").endDate(LocalDateTime.now().plusDays(1)).priority(4).category("Work").status(Status.TODO).build();
        when(taskDAO.findTaskById(1)).thenReturn(existing);

        // WHEN
        Response<Void> response = taskService.deleteTask(1);

        // THEN
        assertEquals(204, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO).deleteTask(1);
    }

    @Test
    void deleteTaskInvalidId() {
        // GIVEN
        int taskId = -3;

        // WHEN
        Response<Void> response = taskService.deleteTask(taskId);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO, never()).deleteTask(anyInt());
    }

    @Test
    void deleteTaskNoTask() {
        // GIVEN
        when(taskDAO.findTaskById(1)).thenReturn(null);

        // WHEN
        Response<Void> response = taskService.deleteTask(1);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
        verify(taskDAO).findTaskById(1);
        verify(taskDAO, never()).deleteTask(anyInt());
    }
}
