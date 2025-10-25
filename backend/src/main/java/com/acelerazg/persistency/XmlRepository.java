package com.acelerazg.persistency;

import com.acelerazg.model.Task;
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
import java.util.LinkedHashMap;
import java.util.Map;

import static com.acelerazg.persistency.XmlUtilities.createTaskElement;
import static com.acelerazg.persistency.XmlUtilities.parseTask;

public class XmlRepository {
    public void saveTasksToXml(Map<Integer, Task> tasks, int nextTaskId, int nextReminderId, String filePath) throws Exception {
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

    public XmlData loadTasksFromXml(String filePath) throws Exception {
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

}
