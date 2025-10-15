package com.acelerazg.exceptions;

public class CancelOperationException extends RuntimeException {
    public CancelOperationException() {
        super("Operation cancelled, returning to main menu.");
    }
}
