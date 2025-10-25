package com.acelerazg.persistency;

import com.acelerazg.model.Reminder;
import com.acelerazg.model.Status;
import com.acelerazg.model.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;

public class XmlUtilities {
    protected static Element createTaskElement(Document doc, Task task) {
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

    protected static Element createReminderElement(Document doc, Reminder reminder) {
        Element reminderElem = doc.createElement("reminder");
        reminderElem.setAttribute("id", String.valueOf(reminder.getId()));
        appendChild(doc, reminderElem, "message", reminder.getMessage());
        appendChild(doc, reminderElem, "hoursInAdvance", String.valueOf(reminder.getHoursInAdvance()));
        return reminderElem;
    }

    protected static Task parseTask(Element taskElem) {
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

        Task task = Task.builder().id(id).name(name).description(description).endDate(endDate).priority(priority).category(category).creationDate(creationDate).modificationDate(modificationDate).status(status).build();

        NodeList reminderNodes = taskElem.getElementsByTagName("reminder");
        for (int j = 0; j < reminderNodes.getLength(); j++) {
            Element reminderElem = (Element) reminderNodes.item(j);
            Reminder reminder = parseReminder(reminderElem);
            task.getReminders().put(reminder.getId(), reminder);
        }

        return task;
    }

    protected static Reminder parseReminder(Element reminderElem) {
        int reminderId = Integer.parseInt(reminderElem.getAttribute("id"));
        String message = getChildText(reminderElem, "message");
        int hoursInAdvance = Integer.parseInt(getChildText(reminderElem, "hoursInAdvance"));
        return Reminder.builder().id(reminderId).message(message).hoursInAdvance(hoursInAdvance).build();
    }

    protected static void appendChild(Document doc, Element parent, String tag, String text) {
        Element elem = doc.createElement(tag);
        elem.appendChild(doc.createTextNode(text != null ? text : ""));
        parent.appendChild(elem);
    }

    protected static String getChildText(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
}