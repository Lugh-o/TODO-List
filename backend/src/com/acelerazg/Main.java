package com.acelerazg;

import com.acelerazg.todolist.task.Status;
import com.acelerazg.todolist.TodoList;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        TodoList list = new TodoList();
        System.out.println(list.createTask("fazer o app", "tarefa de fazer o app", LocalDate.of(2025, 8, 15), 1, "backend", Status.TODO));
        list.createTask("fazer o app", "tarefa de fazer o app", LocalDate.of(2025, 8, 15), 1, "backend", Status.TODO);
        list.createTask("fazer o app", "tarefa de fazer o app", LocalDate.of(2025, 8, 15), 1, "backend", Status.TODO);
        list.createTask("fazer o app", "tarefa de fazer o app", LocalDate.of(2025, 8, 15), 1, "backend", Status.TODO);
        list.createTask("fazer o app", "tarefa de fazer o app", LocalDate.of(2025, 8, 15), 1, "backend", Status.TODO);

        System.out.println(list.deleteTask(2));
        System.out.println(list.getTaskById(2));
    }
}
