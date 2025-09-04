package com.acelerazg.todolist.exceptions;

/**
 * Exception thrown when a user cancels an operation.
 * <p>
 * It can be used to interrupt the current operation and return to the main menu.
 */
public class CancelOperationException extends RuntimeException {
    public CancelOperationException() {
        super("Operation cancelled, returning to main menu.");
    }
}
