package com.acelerazg;

import com.acelerazg.todolist.TodoList;
import com.acelerazg.todolist.common.Messages;
import com.acelerazg.todolist.common.Response;
import com.acelerazg.todolist.task.Status;
import com.acelerazg.todolist.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        TodoList todoList = new TodoList();
        String input = "";

        try (Scanner scanner = new Scanner(System.in)) {
            while (!Objects.equals(input, "7")) {
                System.out.println(Messages.MENU_OPTIONS);
                input = scanner.nextLine().trim();

                switch (input) {
                    case "1":
                        handleListAllTasks(todoList);
                        break;
                    case "2":
                        handleGetTaskById(todoList, scanner);
                        break;
                    case "3":
                        handleCreateTask(todoList, scanner);
                        break;
                    case "4":
                        handleUpdateTask(todoList, scanner);
                        break;
                    case "5":
                        handleDeleteTask(todoList, scanner);
                        break;
                    case "6":
                        handleFilterTasks(todoList, scanner);
                        break;
                    case "7":
                        System.out.println(Messages.EXITING_APP);
                        break;
                    default:
                        System.out.println(Messages.INVALID_OPTION);
                        break;
                }
            }
        }
    }

    private static void handleListAllTasks(TodoList todoList) {
        printTaskListResponse(todoList.getAllTasks());
    }

    private static void handleGetTaskById(TodoList todoList, Scanner scanner) {
        System.out.print(Messages.PROMPT_TASK_ID);
        int id = readInt(scanner);
        printSingleTaskResponse(todoList.getTaskById(id));
    }

    private static void handleCreateTask(TodoList todoList, Scanner scanner) {
        System.out.print(Messages.PROMPT_NAME);
        String name = scanner.nextLine();

        System.out.print(Messages.PROMPT_DESCRIPTION);
        String description = scanner.nextLine();

        System.out.print(Messages.PROMPT_END_DATE);
        LocalDate endDate = readDate(scanner);

        System.out.print(Messages.PROMPT_PRIORITY);
        int priority = readInt(scanner);

        System.out.print(Messages.PROMPT_CATEGORY);
        String category = scanner.nextLine();

        System.out.print(Messages.PROMPT_STATUS);
        Status status = readStatus(scanner);

        printSingleTaskResponse(todoList.createTask(name, description, endDate, priority, category, status));
    }

    private static void handleUpdateTask(TodoList todoList, Scanner scanner) {
        System.out.print(Messages.PROMPT_UPDATE_ID);
        int id = readInt(scanner);

        System.out.print(Messages.PROMPT_NEW_NAME);
        String name = scanner.nextLine();

        System.out.print(Messages.PROMPT_NEW_DESCRIPTION);
        String description = scanner.nextLine();

        System.out.print(Messages.PROMPT_NEW_END_DATE);
        LocalDate endDate = readOptionalDate(scanner);

        System.out.print(Messages.PROMPT_NEW_PRIORITY);
        Integer priority = readOptionalInt(scanner);

        System.out.print(Messages.PROMPT_NEW_CATEGORY);
        String category = scanner.nextLine();

        System.out.print(Messages.PROMPT_NEW_STATUS);
        Status status = readOptionalStatus(scanner);

        printSingleTaskResponse(todoList.updateTask(id, name, description, endDate, priority, category, status));
    }

    private static void handleDeleteTask(TodoList todoList, Scanner scanner) {
        System.out.print(Messages.PROMPT_DELETE_ID);
        int id = readInt(scanner);
        Response<Void> response = todoList.deleteTask(id);
        System.out.println(response.getMessage());
    }

    //TODO
    private static void handleFilterTasks(TodoList todoList, Scanner scanner) {
    }

    private static void printTaskListResponse(Response<Map<Integer, Task>> response) {
        System.out.println(response.getMessage());
        if (response.getStatusCode() >= 400) {
            return;
        }
        if (response.getData().isEmpty()) {
            System.out.println(Messages.ERROR_NO_TASKS_FOUND);
        } else {
            response.getData().values().forEach(System.out::println);
        }
    }

    private static void printSingleTaskResponse(Response<Task> response) {
        System.out.println(response.getMessage());
        if (response.getStatusCode() < 400 && response.getData() != null) {
            System.out.println(response.getData());
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(Messages.ERROR_INVALID_NUMBER);
            }
        }
    }

    private static Integer readOptionalInt(Scanner scanner) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return null;
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static LocalDate readDate(Scanner scanner) {
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine().trim(), DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.print(Messages.ERROR_INVALID_DATE);
            }
        }
    }

    private static LocalDate readOptionalDate(Scanner scanner) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return null;
        try {
            return LocalDate.parse(input, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static Status readStatus(Scanner scanner) {
        while (true) {
            try {
                return Status.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.print(Messages.ERROR_INVALID_STATUS);
            }
        }
    }

    private static Status readOptionalStatus(Scanner scanner) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return null;
        try {
            return Status.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}