package com.acelerazg.dto;

import com.acelerazg.model.Status;

import java.time.LocalDateTime;

public class CreateTaskDTO {
    private String name;
    private String description;
    private LocalDateTime endDate;
    private int priority;
    private String category;
    private Status status;

    public CreateTaskDTO(String name, String description, LocalDateTime endDate, int priority, String category, Status status) {
        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.priority = priority;
        this.category = category;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
