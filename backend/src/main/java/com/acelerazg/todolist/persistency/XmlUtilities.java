package com.acelerazg.todolist.persistency;

import com.acelerazg.todolist.task.Reminder;
import com.acelerazg.todolist.task.Status;
import com.acelerazg.todolist.task.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for XML persistence of tasks.
 * <p>
 * Contains methods to save and load tasks and their reminders to an XML file.
 */
public class XmlUtilities {

    public static void saveTasksToXml(Map<Integer, Task> tasks, int nextTaskId, int nextReminderId, String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("tasks");
        root.setAttribute("nextTaskId", String.valueOf(nextTaskId));
        root.setAttribute("nextReminderId", String.valueOf(nextReminderId));
        doc.appendChild(root);

        for (Task task : tasks.values()) {
            Element taskElem = createTaskElement(doc, task);
            root.appendChild(taskElem);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(new DOMSource(doc), new StreamResult(new File(filePath)));
    }

    public static XmlData loadTasksFromXml(String filePath) throws Exception {
        Map<Integer, Task> tasks = new LinkedHashMap<>();
        int nextTaskId = 1;
        int nextReminderId = 1;

        File file = new File(filePath);
        if (!file.exists()) {
            return new XmlData(tasks, nextTaskId, nextReminderId);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        if (root.hasAttribute("nextTaskId")) {
            nextTaskId = Integer.parseInt(root.getAttribute("nextTaskId"));
        }
        if (root.hasAttribute("nextReminderId")) {
            nextReminderId = Integer.parseInt(root.getAttribute("nextReminderId"));
        }

        NodeList taskNodes = root.getElementsByTagName("task");
        for (int i = 0; i < taskNodes.getLength(); i++) {
            Element taskElem = (Element) taskNodes.item(i);
            Task task = parseTask(taskElem);
            tasks.put(task.getId(), task);
        }

        return new XmlData(tasks, nextTaskId, nextReminderId);
    }

    private static Element createTaskElement(Document doc, Task task) {
        Element taskElem = doc.createElement("task");
        taskElem.setAttribute("id", String.valueOf(task.getId()));

        appendChild(doc, taskElem, "name", task.getName());
        appendChild(doc, taskElem, "description", task.getDescription());
        appendChild(doc, taskElem, "endDate", task.getEndDate() != null ? task.getEndDate().toString() : "");
        appendChild(doc, taskElem, "priority", String.valueOf(task.getPriority()));
        appendChild(doc, taskElem, "category", task.getCategory());
        appendChild(doc, taskElem, "status", task.getStatus().name());
        appendChild(doc, taskElem, "creationDate", task.getCreationDate().toString());
        appendChild(doc, taskElem, "modificationDate", task.getModificationDate().toString());

        Element remindersElem = doc.createElement("reminders");
        taskElem.appendChild(remindersElem);

        for (Reminder reminder : task.getReminders().values()) {
            Element reminderElem = createReminderElement(doc, reminder);
            remindersElem.appendChild(reminderElem);
        }

        return taskElem;
    }

    private static Element createReminderElement(Document doc, Reminder reminder) {
        Element reminderElem = doc.createElement("reminder");
        reminderElem.setAttribute("id", String.valueOf(reminder.getId()));
        appendChild(doc, reminderElem, "message", reminder.getMessage());
        appendChild(doc, reminderElem, "hoursInAdvance", String.valueOf(reminder.getHoursInAdvance()));
        return reminderElem;
    }

    private static Task parseTask(Element taskElem) {
        int id = Integer.parseInt(taskElem.getAttribute("id"));
        String name = getChildText(taskElem, "name");
        String description = getChildText(taskElem, "description");
        String endDateText = getChildText(taskElem, "endDate");
        LocalDateTime endDate = endDateText.isEmpty() ? null : LocalDateTime.parse(endDateText);
        int priority = Integer.parseInt(getChildText(taskElem, "priority"));
        String category = getChildText(taskElem, "category");
        Status status = Status.valueOf(getChildText(taskElem, "status"));
        LocalDateTime creationDate = LocalDateTime.parse(getChildText(taskElem, "creationDate"));
        LocalDateTime modificationDate = LocalDateTime.parse(getChildText(taskElem, "modificationDate"));

        Task task = new Task(id, name, description, endDate, priority, category, status);
        task.setCreationDate(creationDate);
        task.setModificationDate(modificationDate);

        NodeList reminderNodes = taskElem.getElementsByTagName("reminder");
        for (int j = 0; j < reminderNodes.getLength(); j++) {
            Element reminderElem = (Element) reminderNodes.item(j);
            Reminder reminder = parseReminder(reminderElem);
            task.getReminders().put(reminder.getId(), reminder);
        }

        return task;
    }

    private static Reminder parseReminder(Element reminderElem) {
        int reminderId = Integer.parseInt(reminderElem.getAttribute("id"));
        String message = getChildText(reminderElem, "message");
        int hoursInAdvance = Integer.parseInt(getChildText(reminderElem, "hoursInAdvance"));
        return new Reminder(reminderId, message, hoursInAdvance);
    }

    private static void appendChild(Document doc, Element parent, String tag, String text) {
        Element elem = doc.createElement(tag);
        elem.appendChild(doc.createTextNode(text != null ? text : ""));
        parent.appendChild(elem);
    }

    private static String getChildText(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
}