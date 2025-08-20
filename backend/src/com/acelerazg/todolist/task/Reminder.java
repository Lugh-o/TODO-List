package com.acelerazg.todolist.task;

public class Reminder {
    private final int id;
    private String message;
    private int hoursInAdvance;

    public Reminder(int id, String message, int hoursInAdvance) {
        this.id = id;
        this.message = message;
        this.hoursInAdvance = hoursInAdvance;
    }

    public int getHoursInAdvance() {
        return hoursInAdvance;
    }

    public void setHoursInAdvance(int hoursInAdvance) {
        this.hoursInAdvance = hoursInAdvance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", hoursInAdvance=" + hoursInAdvance +
                ", message='" + message + '\'' +
                '}';
    }
}
