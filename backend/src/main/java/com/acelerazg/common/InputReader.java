package com.acelerazg.common;

import com.acelerazg.exceptions.CancelOperationException;
import com.acelerazg.model.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputReader {
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    protected static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String nextLine() {
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }

    public int readInt() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(Messages.ERROR_INVALID_NUMBER);
            }
        }
    }

    public String readNonEmptyString(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (value.equalsIgnoreCase("q")) throw new CancelOperationException();
            if (!value.isEmpty()) return value;
            System.out.println(errorMessage);
        }
    }

    public LocalDateTime readDateTimeNotInPast() {
        while (true) {
            System.out.print(Messages.PROMPT_END_DATE_TIME);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            if (input.isEmpty()) {
                System.out.println(Messages.ERROR_END_DATE_EMPTY);
                continue;
            }
            try {
                LocalDateTime date = LocalDateTime.parse(input, DATE_TIME_FORMAT);
                if (!date.isBefore(LocalDateTime.now())) return date;
                System.out.println(Messages.ERROR_END_DATE_PAST);
            } catch (DateTimeParseException e) {
                System.out.println(Messages.ERROR_INVALID_DATE_TIME);
            }
        }
    }

    public int readPriority() {
        while (true) {
            System.out.print(Messages.PROMPT_PRIORITY);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            try {
                int value = Integer.parseInt(input);
                if (value >= 1 && value <= 5) return value;
                System.out.println(Messages.ERROR_PRIORITY_RANGE);
            } catch (NumberFormatException e) {
                System.out.println(Messages.ERROR_INVALID_NUMBER);
            }
        }
    }

    public Status readStatus() {
        while (true) {
            System.out.print(Messages.PROMPT_STATUS);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            if (input.isEmpty()) {
                System.out.println(Messages.ERROR_EMPTY_STATUS);
                continue;
            }
            try {
                return Status.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(Messages.ERROR_INVALID_STATUS);
            }
        }
    }

    public String readOptionalNonEmptyString(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            String trimmed = input.trim();
            if (input.isEmpty()) return null;
            if (!trimmed.isEmpty()) return trimmed;
            System.out.println(errorMessage);
        }
    }

    public LocalDateTime readOptionalDateTimeNotInPast() {
        while (true) {
            System.out.print(Messages.PROMPT_NEW_END_DATE_TIME);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            if (input.isEmpty()) return null;
            try {
                LocalDateTime date = LocalDateTime.parse(input, DATE_TIME_FORMAT);
                if (!date.isBefore(LocalDateTime.now())) return date;
                System.out.println(Messages.ERROR_END_DATE_PAST);
            } catch (DateTimeParseException e) {
                System.out.println(Messages.ERROR_INVALID_DATE_TIME);
            }
        }
    }

    public Integer readOptionalPriority() {
        while (true) {
            System.out.print(Messages.PROMPT_NEW_PRIORITY);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            if (input.isEmpty()) return null;
            try {
                int value = Integer.parseInt(input);
                if (value >= 1 && value <= 5) return value;
                System.out.println(Messages.ERROR_PRIORITY_RANGE);
            } catch (NumberFormatException e) {
                System.out.println(Messages.ERROR_INVALID_NUMBER);
            }
        }
    }

    public Status readOptionalStatus() {
        while (true) {
            System.out.print(Messages.PROMPT_NEW_STATUS);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            if (input.isEmpty()) return null;
            try {
                return Status.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(Messages.ERROR_INVALID_STATUS);
            }
        }
    }

    public LocalDate readDateNotInPast() {
        while (true) {
            System.out.print(Messages.PROMPT_END_DATE);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            if (input.isEmpty()) {
                System.out.println(Messages.ERROR_END_DATE_EMPTY);
                continue;
            }
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMAT);
                if (!date.isBefore(LocalDate.now())) return date;
                System.out.println(Messages.ERROR_END_DATE_PAST);
            } catch (DateTimeParseException e) {
                System.out.println(Messages.ERROR_INVALID_DATE);
            }
        }
    }

    public int readReminderHoursInAdvance() {
        while (true) {
            System.out.print(Messages.PROMPT_REMINDER_ANTECEDENCY);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                throw new CancelOperationException();
            }
            try {
                int value = Integer.parseInt(input);
                if (value >= 1) {
                    return value;
                }
                System.out.println(Messages.ERROR_REMINDER_HOURS_RANGE);
            } catch (NumberFormatException e) {
                System.out.println(Messages.ERROR_INVALID_NUMBER);
            }
        }
    }

    public Integer readOptionalReminderHoursInAdvance() {
        while (true) {
            System.out.print(Messages.PROMPT_UPDATE_REMINDER_ANTECEDENCY);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) throw new CancelOperationException();
            if (input.isEmpty()) return null;
            try {
                int value = Integer.parseInt(input);
                if (value >= 1) return value;

                System.out.println(Messages.ERROR_REMINDER_HOURS_RANGE);
            } catch (NumberFormatException e) {
                System.out.println(Messages.ERROR_INVALID_NUMBER);
            }
        }
    }

}
