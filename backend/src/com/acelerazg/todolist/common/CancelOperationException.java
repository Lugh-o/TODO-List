package com.acelerazg.todolist.common;

public class CancelOperationException extends RuntimeException {
    public CancelOperationException() {
        super("Operation cancelled, returning to main menu.");
    }
}
