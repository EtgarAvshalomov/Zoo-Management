package file_connection;

import connections.Cleaning;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CleaningFileManager {
    private static List<Cleaning> cleanings = new ArrayList<>();


    // Helper method to parse a line and add a cleaning to the list
    private static void addCleaningFromLine(String line, DateTimeFormatter dateTimeFormatter) {
        String[] fields = line.split(",");
        if (fields.length == 4) {
            try {
                UUID cageId = UUID.fromString(fields[0]);
                String employeeId = fields[1];
                LocalDateTime startTime = LocalDateTime.parse(fields[2], dateTimeFormatter);
                LocalDateTime endTime = fields[3].equals("null") ? null : LocalDateTime.parse(fields[3], dateTimeFormatter);

                cleanings.add(new Cleaning(cageId, employeeId, startTime, endTime));
            } catch (Exception e) {
                System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            }
        } else {
            System.err.println("Invalid line format: " + line);
        }
    }

    // Adds a cleaning to the list
    public static void addCleaning(Cleaning cleaning) {
        cleanings.add(cleaning);
    }

    // Retrieves the list of cleanings
    public static List<Cleaning> getCleanings() {
        return cleanings;
        
    }

    // Writes the list of cleanings to a file
    public static void writeCleaningsToFile(String filePath) throws Exception {
        // Deletes the file before writing
        if (!FileManager.deleteFile(filePath)) {
            System.err.println("Failed to delete the existing file. Aborting write operation.");
            return;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Cleaning cleaning : cleanings) {
                writer.write(cleaning.getCageId() + "," +
                        cleaning.getEmployeeId() + "," +
                        cleaning.getStartTime().format(dateTimeFormatter) + "," +
                        (cleaning.getEndTime() != null ? cleaning.getEndTime().format(dateTimeFormatter) : "null"));
                writer.newLine();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    // Loads cleanings from a file
    public static void loadCleaningsFromFile(String filePath) {
        // Clears existing cleanings
        cleanings.clear();
        
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                addCleaningFromLine(line, dateTimeFormatter);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
