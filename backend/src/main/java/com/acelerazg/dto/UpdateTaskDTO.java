package com.acelerazg.dto;

import com.acelerazg.model.Status;

import java.time.LocalDateTime;

public class UpdateTaskDTO {
    private int id;
    private String name;
    private String description;
    private LocalDateTime endDate;
    private Integer priority;
    private String category;
    private Status status;

    public UpdateTaskDTO(int id, String name, String description, LocalDateTime endDate, Integer priority, String category, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.priority = priority;
        this.category = category;
        this.status = status;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
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
