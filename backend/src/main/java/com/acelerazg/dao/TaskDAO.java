package com.acelerazg.dao;

import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.model.Reminder;
import com.acelerazg.model.Task;
import com.acelerazg.persistency.XmlData;
import com.acelerazg.persistency.XmlUtilities;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TaskDAO {
    private Map<Integer, Task> tasks;
    private int nextTaskId;
    private int nextReminderId;
    private static final String OUT_DIR = "data";
    private static final String OUT_FILE = "tasks.xml";
    private static final String FILE_PATH = "./" + OUT_DIR + "/" + OUT_FILE;

    public TaskDAO() {
        this.tasks = new HashMap<>();
        this.nextTaskId = 1;
        this.nextReminderId = 1;
    }

    public Map<Integer, Task> getTasks() {
        return new HashMap<>(tasks);
    }

    public Task createTask(Task task) {
        int taskId = generateNextTaskId();
        task.setId(taskId);
        this.tasks.put(taskId, task);
        return task;
    }

    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    public Task updateTask(int id, Task updatedData) {
        Task currentTask = findTaskById(id);
        if (currentTask == null) throw new IllegalArgumentException("Task not found");

        currentTask.setName(updatedData.getName());
        currentTask.setDescription(updatedData.getDescription());
        currentTask.setEndDate(updatedData.getEndDate());
        currentTask.setPriority(updatedData.getPriority());
        currentTask.setCategory(updatedData.getCategory());
        currentTask.setStatus(updatedData.getStatus());
        currentTask.setModificationDate(LocalDateTime.now());

        return currentTask;
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public Map<Integer, Reminder> getReminders(int taskId) {
        Task task = findTaskById(taskId);
        if (task == null) throw new IllegalArgumentException("Task not found");
        return task.getReminders();
    }

    public Reminder findReminderById(int taskId, int reminderId) {
        Map<Integer, Reminder> reminderList = getReminders(taskId);
        return reminderList.get(reminderId);
    }

    public Reminder createReminder(int taskId, Reminder reminder) {
        Task task = findTaskById(taskId);
        if (task == null) throw new IllegalArgumentException("Task not found");

        int reminderId = generateNextReminderId();
        reminder.setId(reminderId);

        task.getReminders().put(reminderId, reminder);
        return reminder;
    }

    public Reminder updateReminder(int taskId, int reminderId, Reminder newReminderData) {
        Task task = findTaskById(taskId);
        if (task == null) throw new IllegalArgumentException("Task not found");

        Reminder reminder = task.getReminders().get(reminderId);
        if (reminder == null) throw new IllegalArgumentException("Reminder not found");

        reminder.setMessage(newReminderData.getMessage());
        reminder.setHoursInAdvance(newReminderData.getHoursInAdvance());
        return reminder;
    }

    public void removeReminder(int taskId, int reminderId) {
        Task task = findTaskById(taskId);
        if (task == null) throw new IllegalArgumentException("Task not found");
        task.getReminders().remove(reminderId);
    }

    private int generateNextTaskId() {
        return nextTaskId++;
    }

    private int generateNextReminderId() {
        return nextReminderId++;
    }


    public Response<Void> saveDataToXml() {
        try {
            File outDir = new File(OUT_DIR);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            XmlUtilities.saveTasksToXml(tasks, nextTaskId, nextReminderId, FILE_PATH);
            return Response.success(200, Messages.SUCCESS_SAVE_DATA, null);
        } catch (Exception e) {
            return Response.error(500, Messages.ERROR_SAVE_DATA);
        }
    }

    public void loadDataFromXml() {
        try {
            XmlData data = XmlUtilities.loadTasksFromXml(FILE_PATH);
            this.tasks = data.getTasks();
            this.nextTaskId = data.getNextTaskId();
            this.nextReminderId = data.getNextReminderId();
            System.out.println(Messages.SUCCESS_LOAD_DATA);
        } catch (Exception e) {
            System.out.println(Messages.ERROR_LOAD_DATA);
        }
    }
}
