package com.acelerazg.controller;

import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;
import com.acelerazg.service.TaskService;
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
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void createTaskSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Task createdTask = Task.builder().id(1).name("Task 1").description("Description").endDate(endDate).priority(4).category("Work").status(Status.TODO).build();

        Response<Task> expectedResponse = Response.success(201, Messages.SUCCESS_TASK_CREATED, createdTask);
        when(taskService.createTask(anyString(), anyString(), any(LocalDateTime.class), anyInt(), anyString(), any(Status.class))).thenReturn(expectedResponse);

        // WHEN
        Response<Task> response = taskController.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // THEN
        assertEquals(201, response.getStatusCode());
        assertEquals(Messages.SUCCESS_TASK_CREATED, response.getMessage());
        assertEquals(createdTask, response.getData());
        verify(taskService).createTask(anyString(), anyString(), any(LocalDateTime.class), anyInt(), anyString(), any(Status.class));
    }

    @Test
    void getTaskByIdSuccessful() {
        // GIVEN
        Task task = Task.builder().id(1).name("Task 1").description("Description").build();

        Response<Task> expectedResponse = Response.success(200, Messages.SUCCESS_TASK_RETRIEVED, task);
        when(taskService.getTaskById(1)).thenReturn(expectedResponse);

        // WHEN
        Response<Task> response = taskController.getTaskById(1);

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(task, response.getData());
        verify(taskService).getTaskById(1);
    }

    @Test
    void getAllTasks() {
        // GIVEN
        Map<Integer, Task> tasks = new HashMap<>();
        Task task = Task.builder().id(1).name("Task 1").build();
        tasks.put(1, task);

        Response<Map<Integer, Task>> expectedResponse = Response.success(200, Messages.SUCCESS_TASKS_RETRIEVED, tasks);
        when(taskService.getAllTasks()).thenReturn(expectedResponse);

        // WHEN
        Response<Map<Integer, Task>> response = taskController.getAllTasks();

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(tasks, response.getData());
        verify(taskService).getAllTasks();
    }


    @Test
    void updateTaskSuccessful() {
        // GIVEN
        Task updatedTask = Task.builder().id(1).name("Updated Task").status(Status.DONE).build();
        Response<Task> expectedResponse = Response.success(200, Messages.SUCCESS_TASK_UPDATED, updatedTask);
        when(taskService.updateTask(eq(1), anyString(), anyString(), any(LocalDateTime.class), anyInt(), anyString(), any(Status.class))).thenReturn(expectedResponse);

        // WHEN
        Response<Task> response = taskController.updateTask(1, "Updated Task", "Desc", LocalDateTime.now().plusDays(1), 4, "Work", Status.DONE);

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(Messages.SUCCESS_TASK_UPDATED, response.getMessage());
        assertEquals(updatedTask, response.getData());
        verify(taskService).updateTask(eq(1), anyString(), anyString(), any(LocalDateTime.class), anyInt(), anyString(), any(Status.class));
    }

    @Test
    void deleteTaskSuccessful() {
        // GIVEN
        Response<Void> expectedResponse = Response.success(204, Messages.SUCCESS_TASK_DELETED, null);
        when(taskService.deleteTask(1)).thenReturn(expectedResponse);

        // WHEN
        Response<Void> response = taskController.deleteTask(1);

        // THEN
        assertEquals(204, response.getStatusCode());
        assertNull(response.getData());
        verify(taskService).deleteTask(1);
    }
}
