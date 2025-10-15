package com.acelerazg.controller;

import com.acelerazg.App;
import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        App app = new App();
        this.taskController = new TaskController(app);
    }

    @Test
    void createTaskSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        // WHEN
        Response<Task> response = taskController.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // THEN
        assertEquals(201, response.getStatusCode());
        assertEquals(Messages.SUCCESS_TASK_CREATED, response.getMessage());

        Task task = response.getData();
        assertNotNull(task);
        assertEquals("Task 1", task.getName());
        assertEquals(1, task.getId());

        assertEquals(task, taskController.getTaskById(task.getId()).getData());
    }

    @Test
    void createTaskEmptyName() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        // WHEN
        Response<Task> response = taskController.createTask("", "Description", endDate, 3, "Work", Status.TODO);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void createTaskPriorityOutOfRange() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        // WHEN
        Response<Task> response = taskController.createTask("Task", "Description", endDate, 20, "Work", Status.TODO);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void createTaskPastEndDate() {
        // GIVEN
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        // WHEN
        Response<Task> response = taskController.createTask("Task", "Description", pastDate, 3, "Work", Status.TODO);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void getTaskByIdSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        taskController.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Task> response = taskController.getTaskById(1);

        // THEN
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void getTaskByIdInvalid() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        taskController.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Task> response = taskController.getTaskById(-1);

        // THEN
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void getTaskByIdNoTask() {
        // WHEN
        Response<Task> response = taskController.getTaskById(1);

        // THEN
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void getAllTasks() {
        // WHEN
        Response<Map<Integer, Task>> response = taskController.getAllTasks();

        // THEN
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void updateTaskSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        taskController.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);
        Task originalTask = taskController.getTaskById(1).getData();

        originalTask = new Task(originalTask.getId(), originalTask.getName(), originalTask.getDescription(), originalTask.getEndDate(), originalTask.getPriority(), originalTask.getCategory(), originalTask.getStatus());


        // WHEN
        Response<Task> response = taskController.updateTask(1, "Task Updated", null, null, null, null, Status.DONE);
        Task newTask = response.getData();

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(originalTask.getDescription(), newTask.getDescription());
        assertNotEquals(originalTask.getName(), newTask.getName());
    }

    @Test
    void updateTaskInvalidId() {
        // WHEN
        Response<Task> response = taskController.updateTask(-13, "Task Updated", null, null, null, null, Status.DONE);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void updateTaskNoTask() {
        // WHEN
        Response<Task> response = taskController.updateTask(2, "Task Updated", null, null, null, null, Status.DONE);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void updateTaskPriorityOutOfRange() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        taskController.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Task> response = taskController.updateTask(1, "Task Updated", null, null, 30, null, Status.DONE);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void updateTaskPastEndDate() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        taskController.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Task> response = taskController.updateTask(1, "Task Updated", null, pastDate, 3, null, Status.DONE);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void deleteTaskSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        taskController.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Void> response = taskController.deleteTask(1);

        // THEN
        assertEquals(204, response.getStatusCode());
        assertEquals(0, taskController.getAllTasks().getData().size());
        assertNull(response.getData());
    }

    @Test
    void deleteTaskInvalidId() {
        // WHEN
        Response<Void> response = taskController.deleteTask(-3);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void deleteTaskNoTask() {
        // WHEN
        Response<Void> response = taskController.deleteTask(1);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
    }
}