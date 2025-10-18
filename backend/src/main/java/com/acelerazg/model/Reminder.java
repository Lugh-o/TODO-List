package com.acelerazg.model;

public class Reminder {
    private int id;
    private String message;
    private int hoursInAdvance;

    public Reminder(String message, int hoursInAdvance) {
        this.message = message;
        this.hoursInAdvance = hoursInAdvance;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" + "id=" + id + ", hoursInAdvance=" + hoursInAdvance + ", message='" + message + '\'' + '}';
    }
}
