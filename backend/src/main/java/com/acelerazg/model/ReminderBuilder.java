package com.acelerazg.model;

public class ReminderBuilder {
    private int id;
    private String message;
    private int hoursInAdvance;

    public ReminderBuilder id(int id) {
        this.id = id;
        return this;
    }

    public ReminderBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ReminderBuilder hoursInAdvance(int hoursInAdvance) {
        this.hoursInAdvance = hoursInAdvance;
        return this;
    }

    public Reminder build() {
        return new Reminder(this.id, this.message, this.hoursInAdvance);
    }
}
