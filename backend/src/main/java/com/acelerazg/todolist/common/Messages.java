package com.acelerazg.todolist.common;


/**
 * Static message class for the application.
 * Contains error, success, prompt messages, and methods to format messages.
 *
 * All messages must be accessed statically.
 */
public final class Messages {
    public static final String ERROR_EMPTY_CATEGORY = "Task category cannot be empty";
    public static final String ERROR_EMPTY_DESCRIPTION = "Task description cannot be empty";
    public static final String ERROR_EMPTY_INPUT = "Invalid input: Value cannot be empty";
    public static final String ERROR_EMPTY_NAME = "Task name cannot be empty";
    public static final String ERROR_EMPTY_STATUS = "Task status cannot be empty";
    public static final String ERROR_END_DATE_EMPTY = "End date cannot be empty";
    public static final String ERROR_END_DATE_PAST = "End date cannot be in the past";
    public static final String ERROR_INVALID_DATE = "Invalid date format, please use yyyy-MM-dd: ";
    public static final String ERROR_INVALID_DATE_TIME = "Invalid date format, please use yyyy-MM-dd HH:mm: ";
    public static final String ERROR_INVALID_ID = "Invalid ID: must be positive.";
    public static final String ERROR_INVALID_INPUT = "Invalid input: Name, description, category and status are required.";
    public static final String ERROR_INVALID_NUMBER = "Invalid number, try again: ";
    public static final String ERROR_INVALID_STATUS = "Invalid status, use TODO, DOING, or DONE: ";
    public static final String ERROR_LOAD_DATA = "Failed to load tasks from XML.";
    public static final String ERROR_NO_TASKS_FOUND = "No tasks found.";
    public static final String ERROR_PRIORITY_RANGE = "Priority must be between 1 and 5";
    public static final String ERROR_REMINDER_EMPTY_MESSAGE = "Reminder message cannot be empty";
    public static final String ERROR_REMINDER_HOURS_RANGE = "Reminder antecedence must be greater than 1 hour.";
    public static final String ERROR_REMINDER_NOT_FOUND = "Reminder not found.";
    public static final String ERROR_REMINDER_RANGE = "Invalid field. Please choose 1, 2 or 3 ";
    public static final String ERROR_SAVE_DATA = "Failed to save tasks to XML.";
    public static final String ERROR_TASK_NOT_FOUND = "Task not found.";

    public static final String SUCCESS_LOAD_DATA = "Tasks loaded from XML successfully.";
    public static final String SUCCESS_REMINDER_CREATED = "Reminder created successfully.";
    public static final String SUCCESS_REMINDER_UPDATED = "Reminder updated successfully.";
    public static final String SUCCESS_SAVE_DATA = "Tasks saved to XML successfully.";
    public static final String SUCCESS_TASK_COUNT = "Task count retrieved successfully.";
    public static final String SUCCESS_TASK_CREATED = "Task created successfully.";
    public static final String SUCCESS_TASK_DELETED = "Task deleted successfully.";
    public static final String SUCCESS_TASK_RETRIEVED = "Task retrieved successfully.";
    public static final String SUCCESS_TASKS_RETRIEVED = "Tasks retrieved successfully.";
    public static final String SUCCESS_TASK_UPDATED = "Task updated successfully.";
    public static final String SUCCESS_VALIDATION_PASSED = "Validation passed.";

    public static final String EXITING_APP = "Exiting application...";
    public static final String INVALID_OPTION = "Invalid option. Please choose between 1 and 8.";
    public static final String MENU_OPTIONS = "Choose the operation you wish to do:\n" +
            "1 - List all tasks\n" +
            "2 - Get a task by Id\n" +
            "3 - Create a new task\n" +
            "4 - Update a task by Id\n" +
            "5 - Delete a task by Id\n" +
            "6 - List tasks with a filter\n" +
            "7 - Check task count by status\n" +
            "8 - Manage reminders for a task\n" +
            "q - Exit application and save\n" +
            "You can cancel an operation by submitting 'q'";
    public static final String PROMPT_FILTER_RANGE = "Invalid field. Please choose 1, 2, 3 or 4.";
    public static final String PROMPT_REMINDER_OPTIONS = "Choose the operation you wish to do: \n" +
            "1 - Create a new Reminder \n" +
            "2 - Update a Reminder by Id \n" +
            "3 - Delete a Reminder by Id";

    public static final String PROMPT_CATEGORY = "Category: ";
    public static final String PROMPT_DELETE_ID = "Enter task ID to delete: ";
    public static final String PROMPT_DELETE_REMINDER_ID = "Enter reminder ID to delete: ";
    public static final String PROMPT_DESCRIPTION = "Description: ";
    public static final String PROMPT_END_DATE = "End date (yyyy-MM-dd): ";
    public static final String PROMPT_END_DATE_TIME = "End date (yyyy-MM-dd HH:mm): ";
    public static final String PROMPT_FILTER_FIELD = "Enter field to filter: \n" +
            "1 - Priority\n" +
            "2 - Status\n" +
            "3 - Category\n" +
            "4 - End Date";
    public static final String PROMPT_NAME = "Name: ";
    public static final String PROMPT_NEW_CATEGORY = "New category (leave blank to keep current): ";
    public static final String PROMPT_NEW_DESCRIPTION = "New description (leave blank to keep current): ";
    public static final String PROMPT_NEW_END_DATE_TIME = "New end date time (yyyy-MM-dd HH:mm, blank to keep current): ";
    public static final String PROMPT_NEW_NAME = "New name (leave blank to keep current): ";
    public static final String PROMPT_NEW_PRIORITY = "New priority (1-5, blank to keep current): ";
    public static final String PROMPT_NEW_STATUS = "New status (TODO, DOING, DONE, blank to keep current): ";
    public static final String PROMPT_PRIORITY = "Priority (1-5): ";
    public static final String PROMPT_REMINDER_ANTECEDENCY = "Hours in advance to the task end date: ";
    public static final String PROMPT_REMINDER_ID = "Enter Task ID to manage its reminders: ";
    public static final String PROMPT_REMINDER_MESSAGE = "Reminder Message: ";
    public static final String PROMPT_STATUS = "Status (TODO, DOING, DONE): ";
    public static final String PROMPT_TASK_ID = "Enter task ID: ";
    public static final String PROMPT_UPDATE_ID = "Enter task ID to update: ";
    public static final String PROMPT_UPDATE_REMINDER_ANTECEDENCY = "Hours in advance to the task end date (leave blank to keep current): ";
    public static final String PROMPT_UPDATE_REMINDER_ID = "Enter reminder ID to update: ";
    public static final String PROMPT_UPDATE_REMINDER_MESSAGE = "Reminder Message (leave blank to keep current): ";

    public static final String TRIGGERED_REMINDERS_HEADER = "\n=== Triggered Reminders ===";
    public static final String TRIGGERED_REMINDER_TASK_INFO = "Task ID: %d | End Date: %s";
    public static final String TRIGGERED_REMINDER_INFO = "  - [Reminder ID %d] %s (%dh before)\n";
    public static final String REMINDERS = "Reminders:";

    private Messages() {}

    public static String StatusCountMessage(int todoCount, int doingCount, int doneCount) {
        return "TODO: " + todoCount + "\n" +
                "DOING: " + doingCount + "\n" +
                "DONE: " + doneCount;
    }

    public static String formatTriggeredReminderTaskInfo(int taskId, String endDate) {
        return String.format(TRIGGERED_REMINDER_TASK_INFO, taskId, endDate);
    }

    public static String formatTriggeredReminderInfo(int reminderId, String message, int hoursInAdvance) {
        return String.format(TRIGGERED_REMINDER_INFO, reminderId, message, hoursInAdvance);
    }
}