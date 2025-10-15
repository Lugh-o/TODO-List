package com.acelerazg.common;

import com.acelerazg.model.Status;

import java.time.LocalDateTime;

public class DataValidator {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static Response<Void> validateTaskData(String name, String description, String category, Status status, int priority, LocalDateTime endDate, boolean fullValidation) {
        if (fullValidation && (isNullOrEmpty(name) || isNullOrEmpty(description) || isNullOrEmpty(category) || status == null)) {
            return Response.error(400, Messages.ERROR_INVALID_INPUT);
        }
        if (isNullOrEmpty(name)) return Response.error(400, Messages.ERROR_EMPTY_NAME);
        if (priority < 1 || priority > 5) return Response.error(422, Messages.ERROR_PRIORITY_RANGE);
        if (endDate != null && endDate.isBefore(LocalDateTime.now()))
            return Response.error(422, Messages.ERROR_END_DATE_PAST);
        return Response.success(200, Messages.SUCCESS_VALIDATION_PASSED, null);
    }

    public static Response<Void> validateReminderData(String message, int hoursInAdvance) {
        if (isNullOrEmpty(message)) return Response.error(400, Messages.ERROR_REMINDER_EMPTY_MESSAGE);
        if (hoursInAdvance <= 0) return Response.error(422, Messages.ERROR_REMINDER_HOURS_RANGE);
        return Response.success(200, Messages.SUCCESS_VALIDATION_PASSED, null);
    }
}
