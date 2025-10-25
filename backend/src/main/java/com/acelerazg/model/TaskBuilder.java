package com.acelerazg.model;

import java.time.LocalDateTime;

public class TaskBuilder {
    private int id;
    private String name;
    private String description;
    private LocalDateTime endDate;
    private int priority;
    private String category;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private Status status;

    public TaskBuilder id(int id) {
        this.id = id;
        return this;
    }

    public TaskBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder description(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder endDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public TaskBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public TaskBuilder category(String category) {
        this.category = category;
        return this;
    }

    public TaskBuilder creationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public TaskBuilder modificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
        return this;
    }

    public TaskBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public Task build() {
        LocalDateTime creationDate = this.creationDate;
        if (creationDate == null) creationDate = LocalDateTime.now();

        LocalDateTime modificationDate = this.modificationDate;
        if (modificationDate == null) modificationDate = LocalDateTime.now();

        return new Task(this.id, this.name, this.description, this.endDate, this.priority, this.category, creationDate, modificationDate, this.status);
    }
}
