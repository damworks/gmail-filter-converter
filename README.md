# Gmail Filter Converter

## Overview

Gmail Filter Converter is a simple Java application that converts a Gmail filters XML file into a CSV file. The program reads the exported XML from Gmail, processes the filter data, and generates a CSV file with relevant information such as sender, subject, labels, and other properties.

## Features
- Convert Gmail filter exports (XML) to CSV format
- Extracts important fields like `From`, `Subject`, `Label`, `Should Never Spam`, `Should Archive`, and more
- Generates CSV file with timestamp for easy versioning

## Prerequisites

Make sure you have the following installed on your machine:

- **Java JDK 8 or higher**: You can download it [here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
- **Gradle**: The project uses Gradle as the build system, but you don't need to install Gradle manually, as the project includes the Gradle wrapper (`gradlew`).
- **Git**: If you want to clone the project from GitHub.

## Getting Started

### 1. Clone the repository

To get started, clone the repository from GitHub:

```bash
git clone https://github.com/damworks/gmail-filter-converter.git
```
Navigate to the project directory:
```bash
cd gmail-filter-converter
```

### 2. Build the project

You can build the project using the included Gradle wrapper. Run the following command:
- **On Linux or macOS**:
```bash
./gradlew build
```
- **On Windows**:
```bash
gradlew.bat build
```

### 3. Running the application

Once the project is built, you can run the application. The application expects an XML file exported from Gmail containing filters. The file **must be named `mailFilters.xml`** and placed in a `data` folder inside the project directory.

Then, run the program with the following command:

```bash
java -jar build/libs/gmail-filter-converter.jar
```
The program will process the XML file from the `data` folder and generate a CSV file with the extracted information, saved in the same `data` folder.

### 4. Output

The CSV file will be named `filters_yyyyMMdd_HHmmss.csv` (with the current date and time), and will contain the following columns:

- ID
- Updated
- From
- Subject
- Label
- Should Never Spam
- Should Archive

### Example

If you provide an XML file with Gmail filters like this:

```xml
<entry>
    <id>tag:mail.google.com,2008:filter:12345</id>
    <updated>2024-10-20T12:34:56Z</updated>
    <apps:property name="from" value="someone@example.com" />
    <apps:property name="shouldNeverSpam" value="true" />
    <apps:property name="shouldArchive" value="false" />
</entry>
```
The resulting CSV file will look like this:

```csv
ID;Updated;From;Subject;Label;Should Never Spam;Should Archive
tag:mail.google.com,2008:filter:12345;2024-10-20T12:34:56Z;someone@example.com;;true;false
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
