package com.acelerazg.todolist.persistency;

import com.acelerazg.todolist.task.Task;
import java.util.Map;

public class XmlData {
    private final Map<Integer, Task> tasks;
    private final int nextTaskId;
    private final int nextReminderId;


    public XmlData(Map<Integer, Task> tasks, int nextTaskId, int nextReminderId) {
        this.tasks = tasks;
        this.nextTaskId = nextTaskId;
        this.nextReminderId = nextReminderId;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public int getNextTaskId() {
        return nextTaskId;
    }

    public int getNextReminderId() {
        return nextReminderId;
    }
}
