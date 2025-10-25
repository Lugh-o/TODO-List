package com.acelerazg.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Task implements Comparable<Task> {
    private int id;
    private String name;
    private String description;
    private LocalDateTime endDate;
    private int priority;
    private String category;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private Status status;
    private Map<Integer, Reminder> reminders = new HashMap<>();

    public Task(int id, String name, String description, LocalDateTime endDate, int priority, String category, LocalDateTime creationDate, LocalDateTime modificationDate, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.endDate = endDate;

        if (priority > 5) {
            this.priority = 5;
        } else this.priority = Math.max(priority, 0);

        this.category = category;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.status = status;
    }

    public HashMap<Integer, Reminder> getTriggeredReminders() {
        HashMap<Integer, Reminder> triggeredReminders = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        for (Reminder reminder : reminders.values()) {
            LocalDateTime remindTime = endDate.minusHours(reminder.getHoursInAdvance());
            if (remindTime.isBefore(now)) {
                triggeredReminders.put(reminder.getId(), reminder);
            }
        }
        return triggeredReminders;
    }

    @Override
    public int compareTo(Task otherTask) {
        return Integer.compare(getPriority(), otherTask.getPriority());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<Integer, Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(Map<Integer, Reminder> reminders) {
        this.reminders = reminders;
    }

    @Override
    public String toString() {
        return "{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", endDate=" + endDate + ", priority=" + priority + ", category='" + category + '\'' + ", creationDate=" + creationDate + ", modificationDate=" + modificationDate + ", status=" + status + ", reminders=" + reminders.values() + '}';
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }
}