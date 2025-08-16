package com.acelerazg.todolist.persistency;

import com.acelerazg.todolist.task.Task;
import java.util.Map;

public class CsvData {
    private final Map<Integer, Task> tasks;
    private final int nextId;

    public CsvData(Map<Integer, Task> tasks, int nextId) {
        this.tasks = tasks;
        this.nextId = nextId;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public int getNextId() {
        return nextId;
    }
}
