package com.acelerazg.view;

import com.acelerazg.common.InputReader;
import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.controller.ReminderController;
import com.acelerazg.controller.TaskController;
import com.acelerazg.dao.TaskDAO;
import com.acelerazg.dto.CreateTaskDTO;
import com.acelerazg.dto.UpdateTaskDTO;
import com.acelerazg.exceptions.CancelOperationException;
import com.acelerazg.model.Reminder;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;
import com.acelerazg.service.ReminderService;
import com.acelerazg.service.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static com.acelerazg.view.Printer.printSingleTaskResponse;
import static com.acelerazg.view.Printer.printTaskListResponse;

public class Menu {
    private final TaskDAO taskDAO;
    private final TaskController taskController;
    private final ReminderController reminderController;
    private final InputReader inputReader;
    private final Map<String, Runnable> commands = new HashMap<>();

    {
        commands.put("1", this::handleListAllTasks);
        commands.put("2", this::handleGetTaskById);
        commands.put("3", this::handleCreateTask);
        commands.put("4", this::handleUpdateTask);
        commands.put("5", this::handleDeleteTask);
        commands.put("6", this::handleFilterTasks);
        commands.put("7", this::handleTaskCounting);
        commands.put("8", this::handleReminderManagement);

    }

    public Menu() {
        this.taskDAO = new TaskDAO();
        ReminderService reminderService = new ReminderService(taskDAO);
        TaskService taskService = new TaskService(taskDAO);
        this.taskController = new TaskController(taskService);
        this.reminderController = new ReminderController(reminderService);
        this.inputReader = new InputReader(new Scanner(System.in));
    }

    public void run() {
        taskDAO.loadDataFromXml();
        String input = "";
        try {
            while (!Objects.equals(input, "q")) {
                handleReminderTriggering();
                System.out.println(Messages.MENU_OPTIONS);
                input = inputReader.nextLine();
                try {
                    if (Objects.equals(input, "q")) {
                        System.out.println(Messages.EXITING_APP);
                        System.out.println(taskDAO.saveDataToXml().getMessage());
                        return;
                    }

                    Runnable command = commands.get(input);
                    if (command != null) {
                        command.run();
                    } else {
                        System.out.println(Messages.INVALID_OPTION);
                    }

                } catch (CancelOperationException e) {
                    System.out.println(e.getMessage());
                }
            }
        } finally {
            inputReader.close();
        }
    }

    private void handleReminderTriggering() {
        boolean found = false;
        for (Task task : taskController.getAllTasks().getData().values()) {
            if (task.getStatus() != Status.DONE) {
                HashMap<Integer, Reminder> triggeredReminders = task.getTriggeredReminders();
                if (!triggeredReminders.isEmpty()) {
                    if (!found) {
                        System.out.println(Messages.TRIGGERED_REMINDERS_HEADER);
                        found = true;
                    }
                    System.out.println(Messages.formatTriggeredReminderTaskInfo(task.getId(), task.getEndDate().toString()));
                    System.out.println(Messages.REMINDERS);
                    for (Reminder reminder : triggeredReminders.values()) {
                        System.out.println(Messages.formatTriggeredReminderInfo(reminder.getId(), reminder.getMessage(), reminder.getHoursInAdvance()));
                    }
                }
            }
        }
    }

    private void handleListAllTasks() {
        printTaskListResponse(taskController.getAllTasks());
    }

    private void handleGetTaskById() {
        System.out.print(Messages.PROMPT_TASK_ID);
        int id = inputReader.readInt();
        printSingleTaskResponse(taskController.getTaskById(id));
    }

    private void handleCreateTask() {
        String name = inputReader.readNonEmptyString(Messages.PROMPT_NAME, Messages.ERROR_EMPTY_NAME);
        String description = inputReader.readNonEmptyString(Messages.PROMPT_DESCRIPTION, Messages.ERROR_EMPTY_DESCRIPTION);
        LocalDateTime endDate = inputReader.readDateTimeNotInPast();
        int priority = inputReader.readPriority();
        String category = inputReader.readNonEmptyString(Messages.PROMPT_CATEGORY, Messages.ERROR_EMPTY_CATEGORY);
        Status status = inputReader.readStatus();
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(name, description, endDate, priority, category, status);
        printSingleTaskResponse(taskController.createTask(createTaskDTO));
    }

    private void handleUpdateTask() {
        System.out.print(Messages.PROMPT_UPDATE_ID);
        int id = inputReader.readInt();
        String name = inputReader.readOptionalNonEmptyString(Messages.PROMPT_NEW_NAME, Messages.ERROR_EMPTY_NAME);
        String description = inputReader.readOptionalNonEmptyString(Messages.PROMPT_NEW_DESCRIPTION, Messages.ERROR_EMPTY_DESCRIPTION);
        LocalDateTime endDate = inputReader.readOptionalDateTimeNotInPast();
        Integer priority = inputReader.readOptionalPriority();
        String category = inputReader.readOptionalNonEmptyString(Messages.PROMPT_NEW_CATEGORY, Messages.ERROR_EMPTY_CATEGORY);
        Status status = inputReader.readOptionalStatus();
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(id, name, description, endDate, priority, category, status);
        printSingleTaskResponse(taskController.updateTask(updateTaskDTO));
    }

    private void handleDeleteTask() {
        System.out.print(Messages.PROMPT_DELETE_ID);
        int id = inputReader.readInt();
        Response<Void> response = taskController.deleteTask(id);
        System.out.println(response.getMessage());
    }

    private void handleFilterTasks() {
        while (true) {
            System.out.println(Messages.PROMPT_FILTER_FIELD);
            int field = inputReader.readInt();
            switch (field) {
                case 1:
                    int priority = inputReader.readPriority();
                    printTaskListResponse(taskController.getAllTasksByPriority(priority));
                    return;
                case 2:
                    Status status = inputReader.readStatus();
                    printTaskListResponse(taskController.getAllTasksByStatus(status));
                    return;
                case 3:
                    String category = inputReader.readNonEmptyString(Messages.PROMPT_CATEGORY, Messages.ERROR_EMPTY_CATEGORY);
                    printTaskListResponse(taskController.getAllTasksByCategory(category));
                    return;
                case 4:
                    LocalDate date = inputReader.readDateNotInPast();
                    printTaskListResponse(taskController.getTasksByEndDate(date));
                    return;
                default:
                    System.out.println(Messages.PROMPT_FILTER_RANGE);
            }
        }
    }

    private void handleTaskCounting() {
        Map<Status, Integer> statusCount = taskController.getStatusCount().getData();
        System.out.println(Messages.StatusCountMessage(statusCount.get(Status.TODO), statusCount.get(Status.DOING), statusCount.get(Status.DONE)));
    }

    private void handleReminderManagement() {
        System.out.print(Messages.PROMPT_REMINDER_ID);
        int taskId = inputReader.readInt();
        Response<Task> r = taskController.getTaskById(taskId);
        printSingleTaskResponse(r);
        if (r.getStatusCode() != 200) return;
        while (true) {
            System.out.println(Messages.PROMPT_REMINDER_OPTIONS);
            int option = inputReader.readInt();
            switch (option) {
                case 1:
                    String createMessage = inputReader.readNonEmptyString(Messages.PROMPT_REMINDER_MESSAGE, Messages.ERROR_REMINDER_EMPTY_MESSAGE);
                    int createHoursInAdvance = inputReader.readReminderHoursInAdvance();
                    printSingleTaskResponse(reminderController.createReminder(taskId, createMessage, createHoursInAdvance));
                    return;
                case 2:
                    System.out.print(Messages.PROMPT_UPDATE_REMINDER_ID);
                    int reminderId = inputReader.readInt();
                    String updateMessage = inputReader.readOptionalNonEmptyString(Messages.PROMPT_UPDATE_REMINDER_MESSAGE, Messages.ERROR_REMINDER_EMPTY_MESSAGE);
                    Integer updateHoursInAdvance = inputReader.readOptionalReminderHoursInAdvance();
                    printSingleTaskResponse(reminderController.updateReminder(taskId, reminderId, updateMessage, updateHoursInAdvance));
                    return;
                case 3:
                    System.out.print(Messages.PROMPT_DELETE_REMINDER_ID);
                    int deleteReminderId = inputReader.readInt();
                    Response<Void> response = reminderController.deleteReminder(taskId, deleteReminderId);
                    System.out.println(response.getMessage());
                    return;
                default:
                    System.out.println(Messages.ERROR_REMINDER_RANGE);
                    break;
            }
        }
    }
}
