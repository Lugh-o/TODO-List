package com.acelerazg.common;

import com.acelerazg.model.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DataValidatorTest {
    @Test
    void testIsNullOrEmpty() {
        assertTrue(DataValidator.isNullOrEmpty(null));
        assertTrue(DataValidator.isNullOrEmpty(""));
        assertTrue(DataValidator.isNullOrEmpty("  "));
        assertFalse(DataValidator.isNullOrEmpty("valid"));
    }

    @Test
    void testValidateTaskData_EmptyName() {
        Response<Void> response = DataValidator.validateCreateTaskData("", "desc", "cat", Status.TODO, 3, LocalDateTime.now().plusDays(1));
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void testValidateTaskData_Valid() {
        Response<Void> response = DataValidator.validateUpdateTaskData("Task", 3, LocalDateTime.now().plusDays(1));
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void testValidateReminderData_InvalidHours() {
        Response<Void> response = DataValidator.validateReminderData("message", 0);
        assertEquals(422, response.getStatusCode());
    }
}