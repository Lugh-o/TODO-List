package com.acelerazg;

import com.acelerazg.common.Messages;
import com.acelerazg.common.Response;
import com.acelerazg.model.Task;
import com.acelerazg.persistency.XmlData;
import com.acelerazg.persistency.XmlUtilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static final String OUT_DIR = "data";
    private static final String OUT_FILE = "tasks.xml";
    private static final String FILE_PATH = "./" + OUT_DIR + "/" + OUT_FILE;

    private Map<Integer, Task> tasks;
    private int nextTaskId;
    private int nextReminderId;

    public App() {
        this.tasks = new HashMap<>();
        this.nextTaskId = 1;
        this.nextReminderId = 1;
    }

    public Response<Void> saveDataToXml() {
        try {
            File outDir = new File(OUT_DIR);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            XmlUtilities.saveTasksToXml(tasks, nextTaskId, nextReminderId, FILE_PATH);
            return Response.success(200, Messages.SUCCESS_SAVE_DATA, null);
        } catch (Exception e) {
            return Response.error(500, Messages.ERROR_SAVE_DATA);
        }
    }

    public void loadDataFromXml() {
        try {
            XmlData data = XmlUtilities.loadTasksFromXml(FILE_PATH);
            this.tasks = data.getTasks();
            this.nextTaskId = data.getNextTaskId();
            this.nextReminderId = data.getNextReminderId();
            System.out.println(Messages.SUCCESS_LOAD_DATA);
        } catch (Exception e) {
            System.out.println(Messages.ERROR_LOAD_DATA);
        }
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public int generateNextTaskId() {
        return nextTaskId++;
    }

    public int generateNextReminderId() {
        return nextReminderId++;
    }
}
