package com.acelerazg.common;

import com.acelerazg.exceptions.CancelOperationException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputReaderTest {
    @Test
    void readIntValidInput() {
        String input = "42";
        InputReader reader = new InputReader(new Scanner(new StringReader(input)));

        int value = reader.readInt();

        assertEquals(42, value);
    }

    @Test
    void readIntCancelThrows() {
        String input = "q";
        InputReader reader = new InputReader(new Scanner(new StringReader(input)));

        assertThrows(CancelOperationException.class, reader::readInt);
    }

    @Test
    void readDateTimeNotInPastValid() {
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        String input = future.format(InputReader.DATE_TIME_FORMAT) + "\n";
        InputReader reader = new InputReader(new Scanner(new StringReader(input)));

        LocalDateTime value = reader.readDateTimeNotInPast();
        assertEquals(future.getYear(), value.getYear());
        assertEquals(future.getMonth(), value.getMonth());
    }
}
