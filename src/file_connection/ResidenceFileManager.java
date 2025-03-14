package file_connection;

import connections.Residance;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ResidenceFileManager {
    private static List<Residance> residences = new ArrayList<>();

    // Static method to load residences from a file
    public static void loadResidencesFromFile(String filePath) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                addResidenceFromLine(line, dateTimeFormatter);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    // Static mathod to check if a string is a valid UUID
    private static UUID validateUUID(String uuidString) {
        try {
            return UUID.fromString(uuidString.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID: " + uuidString);
        }
    }
    
    private static void addResidenceFromLine(String line, DateTimeFormatter dateTimeFormatter) {
        String[] fields = line.split(",");
        if (fields.length == 4) {
            try {
                UUID animalId = validateUUID(fields[0]);
                UUID cageId = validateUUID(fields[1]);
                LocalDateTime startTime = LocalDateTime.parse(fields[2].trim(), dateTimeFormatter);
                LocalDateTime endTime = fields[3].trim().equals("null") ? null : LocalDateTime.parse(fields[3].trim(), dateTimeFormatter);
    
                residences.add(new Residance(animalId, cageId, startTime, endTime));
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            }
        } else {
            System.err.println("Invalid line format: " + line);
        }
    }
    
    // Static method to retrieve the list of residences
    public static List<Residance> getResidences() {
        return residences;
    }

    // Static method to write the list of residences to a file
    public static void writeResidencesToFile(String filePath) throws Exception {
        // Delete the file before writing
        if (!FileManager.deleteFile(filePath)) {
            System.err.println("Failed to delete the existing file. Aborting write operation.");
            return;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Residance res : residences) {
                writer.write(res.getAnimalId() + "," +
                        res.getCageId() + "," +
                        res.getStartTime().format(dateTimeFormatter) + "," +
                        (res.getEndTime() != null ? res.getEndTime().format(dateTimeFormatter) : "null"));
                writer.newLine();
            }
        } catch (Exception e) {
            throw e;
        }
    }
    // Static mathod to get cage capacity
    public static int getCapacity(UUID cageId) {
        int count = 0;
        for(Residance res : residences) {
            if(res.getCageId().equals(cageId) && res.getEndTime() == null) {
                count += 1;
            }
        }
        return count;
    }

    // Static method to check if an animal is currently in a specific cage
    public static boolean isAnimalInCage(UUID animalId, UUID cageId) {
        for (Residance residence : residences) {
            if (residence.getAnimalId().equals(animalId) && 
                residence.getCageId().equals(cageId) && 
                residence.getEndTime() == null) {
                return true;
            }
        }
        return false;
    }

    // Static method to add a new residence
    public static void addResidence(Residance residence) {
        residences.add(residence);
    }

    // Static method to end all active residences for a specific cage
    public static void removeResidencesForCage(UUID cageId) {
        for (Residance residence : residences) {
            if (residence.getCageId().equals(cageId) && residence.getEndTime() == null) {
                residence.endResidance();
            }
        }
    }
}
