package com.damworks.gmail.converter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GmailFilterConverter {
    public static void main(String[] args) {
        try {
            // Define paths for the XML input and CSV output
            String xmlFilePath = "data/mailFilters.xml";
            String csvFilePath = "data/filters.csv";

            // Perform XML to CSV conversion
            convertXmlToCsv(xmlFilePath, csvFilePath);

            System.out.println("Conversion completed: " + csvFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the current timestamp formatted as 'yyyyMMdd_HHmmss'.
     *
     * @return Formatted timestamp string
     */
    private static String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return LocalDateTime.now().format(formatter);
    }

    /**
     * Converts a Gmail filters XML file to a CSV file.
     *
     * @param xmlFilePath Path to the XML file
     * @param csvFilePath Path to the CSV file
     * @throws Exception if any I/O or parsing errors occur
     */
    public static void convertXmlToCsv(String xmlFilePath, String csvFilePath) throws Exception {
        Document document = parseXml(xmlFilePath);

        // Prepare the CSV header
        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{"ID", "Updated", "From", "Subject", "Label", "Should Never Spam", "Should Archive"});

        // Process each entry in the XML
        NodeList filterNodes = document.getElementsByTagName("entry");
        for (int i = 0; i < filterNodes.getLength(); i++) {
            Node node = filterNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                csvData.add(extractFilterData(element));
            }
        }

        // Write the data to a CSV file
        writeCsv(csvFilePath, csvData);
    }

    /**
     * Parses the XML file and returns the document object.
     *
     * @param xmlFilePath Path to the XML file
     * @return Parsed XML document
     * @throws Exception if parsing fails
     */
    private static Document parseXml(String xmlFilePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(xmlFilePath));
    }

    /**
     * Extracts filter data from an XML element.
     *
     * @param element XML element representing a filter
     * @return Array of filter data fields for CSV
     */
    private static String[] extractFilterData(Element element) {
        String id = getTagValue(element, "id");
        String updated = getTagValue(element, "updated");
        String from = getPropertyValue(element, "from");
        String subject = getPropertyValue(element, "subject");
        String label = getPropertyValue(element, "label");
        String shouldNeverSpam = getPropertyValueOrDefault(element, "shouldNeverSpam", "false");
        String shouldArchive = getPropertyValueOrDefault(element, "shouldArchive", "false");

        return new String[]{id, updated, from, subject, label, shouldNeverSpam, shouldArchive};
    }

    /**
     * Writes CSV data to the specified file.
     *
     * @param csvFilePath Path to the CSV file
     * @param csvData List of data rows to be written
     * @throws Exception if an I/O error occurs
     */
    private static void writeCsv(String csvFilePath, List<String[]> csvData) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath))) {
            for (String[] rowData : csvData) {
                writer.write(String.join(";", rowData));
                writer.newLine();
            }
        }
    }

    /**
     * Retrieves the text content of the specified tag within an element.
     *
     * @param element XML element
     * @param tagName Name of the tag to retrieve
     * @return Text content of the tag, or null if not found
     */
    private static String getTagValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return nodeList.getLength() > 0 ? nodeList.item(0).getTextContent() : null;
    }

    /**
     * Retrieves the value of an apps:property attribute by name.
     *
     * @param element XML element
     * @param propertyName Name of the property
     * @return Property value, or null if not found
     */
    private static String getPropertyValue(Element element, String propertyName) {
        NodeList properties = element.getElementsByTagName("apps:property");
        for (int i = 0; i < properties.getLength(); i++) {
            Element property = (Element) properties.item(i);
            if (property.getAttribute("name").equals(propertyName)) {
                return property.getAttribute("value");
            }
        }
        return null;
    }

    /**
     * Retrieves the value of an apps:property attribute by name, with a default if not found.
     *
     * @param element XML element
     * @param propertyName Name of the property
     * @param defaultValue Default value if the property is not found
     * @return Property value, or the default value if not found
     */
    private static String getPropertyValueOrDefault(Element element, String propertyName, String defaultValue) {
        String value = getPropertyValue(element, propertyName);
        return value != null ? value : defaultValue;
    }

}
