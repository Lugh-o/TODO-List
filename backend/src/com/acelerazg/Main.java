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
        String name = readNonEmptyString(scanner, Messages.PROMPT_NAME, Messages.ERROR_EMPTY_NAME);
        String description = readNonEmptyString(scanner, Messages.PROMPT_DESCRIPTION, Messages.ERROR_EMPTY_DESCRIPTION);
        LocalDate endDate = readOptionalDateNotInPast(scanner, Messages.PROMPT_END_DATE);
        int priority = readPriority(scanner);
        String category = readNonEmptyString(scanner, Messages.PROMPT_CATEGORY, Messages.ERROR_EMPTY_CATEGORY);
        Status status = readStatus(scanner);

        printSingleTaskResponse(todoList.createTask(name, description, endDate, priority, category, status));
    }

    private static void handleUpdateTask(TodoList todoList, Scanner scanner) {
        System.out.print(Messages.PROMPT_UPDATE_ID);
        int id = readInt(scanner);

        String name = readOptionalNonEmptyString(scanner, Messages.PROMPT_NEW_NAME, Messages.ERROR_EMPTY_NAME);
        String description = readOptionalNonEmptyString(scanner, Messages.PROMPT_NEW_DESCRIPTION, Messages.ERROR_EMPTY_DESCRIPTION);
        LocalDate endDate = readOptionalDateNotInPast(scanner, Messages.PROMPT_NEW_END_DATE);
        Integer priority = readOptionalPriority(scanner);
        String category = readOptionalNonEmptyString(scanner, Messages.PROMPT_NEW_CATEGORY, Messages.ERROR_EMPTY_CATEGORY);
        Status status = readOptionalStatus(scanner);

        printSingleTaskResponse(todoList.updateTask(id, name, description, endDate, priority, category, status));
    }

    private static void handleDeleteTask(TodoList todoList, Scanner scanner) {
        System.out.print(Messages.PROMPT_DELETE_ID);
        int id = readInt(scanner);
        Response<Void> response = todoList.deleteTask(id);
        System.out.println(response.getMessage());
    }

    private static void handleFilterTasks(TodoList todoList, Scanner scanner) {
        while (true) {
            System.out.println(Messages.PROMPT_FILTER_FIELD);
            int field = readInt(scanner);
            switch (field) {
                case 1:
                    int priority = readPriority(scanner);
                    printTaskListResponse(todoList.getAllTasksByPriority(priority));
                    return;
                case 2:
                    Status status = readStatus(scanner);
                    printTaskListResponse(todoList.getAllTasksByStatus(status));
                    return;
                case 3:
                    String category = readNonEmptyString(scanner, Messages.PROMPT_CATEGORY, Messages.ERROR_EMPTY_CATEGORY);
                    printTaskListResponse(todoList.getAllTasksByCategory(category));
                    return;
                default:
                    System.out.println(Messages.PROMPT_FILTER_RANGE);
            }
        }
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

    private static String readNonEmptyString(Scanner scanner, String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println(errorMessage);
        }
    }

    private static String readOptionalNonEmptyString(Scanner scanner, String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            String trimmed = input.trim();
            if (input.isEmpty()) {
                return null;
            }
            if (!trimmed.isEmpty()) {
                return trimmed;
            }
            System.out.println(errorMessage);
        }
    }

    private static int readPriority(Scanner scanner) {
        while (true) {
            System.out.print(Messages.PROMPT_PRIORITY);
            int value = readInt(scanner);
            if (value >= 1 && value <= 5) {
                return value;
            }
            System.out.println(Messages.ERROR_PRIORITY_RANGE);
        }
    }

    private static Integer readOptionalPriority(Scanner scanner) {
        while (true) {
            System.out.print(Messages.PROMPT_NEW_PRIORITY);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                int value = Integer.parseInt(input);
                if (value >= 1 && value <= 5) {
                    return value;
                }
                System.out.println(Messages.ERROR_PRIORITY_RANGE);
            } catch (NumberFormatException e) {
                System.out.println(Messages.ERROR_INVALID_NUMBER);
            }
        }
    }

    private static LocalDate readOptionalDateNotInPast(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMAT);
                if (!date.isBefore(LocalDate.now())) {
                    return date;
                }
                System.out.println(Messages.ERROR_END_DATE_PAST);
            } catch (DateTimeParseException e) {
                System.out.println(Messages.ERROR_INVALID_DATE);
            }
        }
    }

    private static Status readStatus(Scanner scanner) {
        while (true) {
            System.out.print(Messages.PROMPT_STATUS);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(Messages.ERROR_EMPTY_STATUS);
                continue;
            }
            try {
                return Status.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(Messages.ERROR_INVALID_STATUS);
            }
        }
    }

    private static Status readOptionalStatus(Scanner scanner) {
        while (true) {
            System.out.print(Messages.PROMPT_NEW_STATUS);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return Status.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(Messages.ERROR_INVALID_STATUS);
            }
        }
    }
}