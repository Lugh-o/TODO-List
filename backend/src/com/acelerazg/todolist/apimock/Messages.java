package com.acelerazg.todolist.apimock;

public final class Messages {

    private Messages() {}

    public static final String ERROR_INVALID_INPUT = "Invalid input: Name, description, category and status are required.";
    public static final String ERROR_INVALID_ID = "Invalid ID: must be positive.";
    public static final String ERROR_TASK_NOT_FOUND = "Task not found.";
    public static final String ERROR_NO_TASKS_FOUND = "No tasks found.";
    public static final String ERROR_EMPTY_NAME = "Task name cannot be empty";
    public static final String ERROR_PRIORITY_RANGE = "Priority must be between 1 and 5";
    public static final String ERROR_END_DATE_PAST = "End date cannot be in the past";

    public static final String SUCCESS_TASK_CREATED = "Task created successfully.";
    public static final String SUCCESS_TASK_RETRIEVED = "Task retrieved successfully.";
    public static final String SUCCESS_TASKS_RETRIEVED = "Tasks retrieved successfully.";
    public static final String SUCCESS_TASK_UPDATED = "Task updated successfully.";
    public static final String SUCCESS_TASK_DELETED = "Task deleted successfully.";
    public static final String SUCCESS_VALIDATION_PASSED = "Validation passed.";
}
