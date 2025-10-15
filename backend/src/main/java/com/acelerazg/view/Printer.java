package com.acelerazg.view;

import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.model.Task;

import java.util.Map;

public class Printer {
    public static void printTaskListResponse(Response<Map<Integer, Task>> response) {
        System.out.println(response.getMessage());
        if (response.getStatusCode() >= 400) {
            return;
        }
        if (response.getData().isEmpty()) {
            System.out.println(Messages.ERROR_NO_TASKS_FOUND);
        } else {
            response.getData().values().forEach(System.out::println);
        }
    }

    public static void printSingleTaskResponse(Response<Task> response) {
        System.out.println(response.getMessage());
        if (response.getStatusCode() < 400 && response.getData() != null) {
            System.out.println(response.getData());
        }
    }
}
