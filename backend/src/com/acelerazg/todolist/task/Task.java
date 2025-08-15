package com.acelerazg.todolist.task;

import java.time.LocalDate;

public class Task implements Comparable<Task> {
    private int id;
    private String name;
    private String description;
    private LocalDate endDate;
    private int priority;
    private String category;
    private LocalDate creationDate;
    private LocalDate modificationDate;
    private Status status;

    public Task(int id, String name, String description, LocalDate endDate, int priority, String category, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.endDate = endDate;

        if(priority > 5){
            this.priority = 5;
        } else this.priority = Math.max(priority, 0);

        this.category = category;
        this.creationDate = LocalDate.now();
        this.modificationDate = LocalDate.now();
        this.status = status;
    }

    @Override
    public int compareTo(Task otherTask) {
        return Integer.compare(getPriority(), otherTask.getPriority());
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

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", endDate=" + endDate +
                ", priority=" + priority +
                ", category='" + category + '\'' +
                ", status=" + status +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                '}';
    }
}