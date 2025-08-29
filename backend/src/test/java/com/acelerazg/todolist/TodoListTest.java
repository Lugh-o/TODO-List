package com.acelerazg.todolist;

import com.acelerazg.todolist.common.Messages;
import com.acelerazg.todolist.common.Response;
import com.acelerazg.todolist.task.Status;
import com.acelerazg.todolist.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TodoListTest {

    private TodoList todoList;

    @BeforeEach
    void setUp() {
        todoList = new TodoList();
    }

    @Test
    void createTaskSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        // WHEN
        Response<Task> response = todoList.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // THEN
        assertEquals(201, response.getStatusCode());
        assertEquals(Messages.SUCCESS_TASK_CREATED, response.getMessage());

        Task task = response.getData();
        assertNotNull(task);
        assertEquals("Task 1", task.getName());
        assertEquals(1, task.getId());

        assertEquals(task, todoList.getTaskById(task.getId()).getData());
    }

    @Test
    void createTaskEmptyName() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        // WHEN
        Response<Task> response = todoList.createTask("", "Description", endDate, 3, "Work", Status.TODO);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void createTaskPriorityOutOfRange() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        // WHEN
        Response<Task> response = todoList.createTask("Task", "Description", endDate, 20, "Work", Status.TODO);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void createTaskPastEndDate() {
        // GIVEN
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        // WHEN
        Response<Task> response = todoList.createTask("Task", "Description", pastDate, 3, "Work", Status.TODO);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void getTaskByIdSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        todoList.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Task> response = todoList.getTaskById(1);

        // THEN
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void getTaskByIdInvalid() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        todoList.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Task> response = todoList.getTaskById(-1);

        // THEN
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void getTaskByIdNoTask() {
        // WHEN
        Response<Task> response = todoList.getTaskById(1);

        // THEN
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void getAllTasks() {
        // WHEN
        Response<Map<Integer, Task>> response = todoList.getAllTasks();

        // THEN
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void updateTaskSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        todoList.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);
        Task originalTask = todoList.getTaskById(1).getData();

        originalTask = new Task(
                originalTask.getId(),
                originalTask.getName(),
                originalTask.getDescription(),
                originalTask.getEndDate(),
                originalTask.getPriority(),
                originalTask.getCategory(),
                originalTask.getStatus()
        );


        // WHEN
        Response<Task> response = todoList.updateTask(1, "Task Updated", null, null, null, null, Status.DONE);
        Task newTask = response.getData();

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals(originalTask.getDescription(), newTask.getDescription());
        assertNotEquals(originalTask.getName(), newTask.getName());
    }

    @Test
    void updateTaskInvalidId() {
        // WHEN
        Response<Task> response = todoList.updateTask(-13, "Task Updated", null, null, null, null, Status.DONE);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void updateTaskNoTask() {
        // WHEN
        Response<Task> response = todoList.updateTask(2, "Task Updated", null, null, null, null, Status.DONE);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void updateTaskPriorityOutOfRange() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        todoList.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Task> response = todoList.updateTask(1, "Task Updated", null, null, 30, null, Status.DONE);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());

    }

    @Test
    void updateTaskPastEndDate() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        todoList.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Task> response = todoList.updateTask(1, "Task Updated", null, pastDate, 3, null, Status.DONE);

        // THEN
        assertEquals(422, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void deleteTaskSuccessful() {
        // GIVEN
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        todoList.createTask("Task 1", "Description", endDate, 4, "Work", Status.TODO);

        // WHEN
        Response<Void> response = todoList.deleteTask(1);

        // THEN
        assertEquals(204, response.getStatusCode());
        assertEquals(0, todoList.getAllTasks().getData().size());
        assertNull(response.getData());
    }

    @Test
    void deleteTaskInvalidId() {
        // WHEN
        Response<Void> response = todoList.deleteTask(-3);

        // THEN
        assertEquals(400, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void deleteTaskNoTask() {
        // WHEN
        Response<Void> response = todoList.deleteTask(1);

        // THEN
        assertEquals(404, response.getStatusCode());
        assertNull(response.getData());
    }

}