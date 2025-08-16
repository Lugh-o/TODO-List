package com.acelerazg.todolist.persistency;

import com.acelerazg.todolist.task.Status;
import com.acelerazg.todolist.task.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class CsvUtilities {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void saveTasksToCsv(Map<Integer, Task> tasks, int nextId, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("nextId=" + nextId);
            writer.newLine();

            writer.write("id,name,description,endDate,priority,category,status,creationDate,modificationDate");
            writer.newLine();

            for (Task task : tasks.values()) {
                String line = String.join(",",
                        String.valueOf(task.getId()),
                        escapeCsv(task.getName()),
                        escapeCsv(task.getDescription()),
                        task.getEndDate() != null ? task.getEndDate().format(formatter) : "",
                        String.valueOf(task.getPriority()),
                        escapeCsv(task.getCategory()),
                        task.getStatus().name(),
                        task.getCreationDate().format(formatter),
                        task.getModificationDate().format(formatter)
                );
                writer.write(line);
                writer.newLine();
            }
        }
    }


    public static CsvData loadTasksFromCsv(String filePath) throws IOException {
        Map<Integer, Task> tasks = new LinkedHashMap<>();
        int nextId = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();
            if (firstLine != null && firstLine.startsWith("nextId=")) {
                nextId = Integer.parseInt(firstLine.substring("nextId=".length()));
            }
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCsvLine(line);

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String description = parts[2];
                LocalDateTime endDate = parts[3].isEmpty() ? null : LocalDateTime.parse(parts[3], formatter);
                int priority = Integer.parseInt(parts[4]);
                String category = parts[5];
                Status status = Status.valueOf(parts[6]);
                LocalDateTime creationDate = LocalDateTime.parse(parts[7], formatter);
                LocalDateTime modificationDate = LocalDateTime.parse(parts[8], formatter);

                Task task = new Task(id, name, description, endDate, priority, category, status);
                task.setCreationDate(creationDate);
                task.setModificationDate(modificationDate);
                tasks.put(id, task);
            }
        }
        return new CsvData(tasks, nextId);
    }

    private static String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
