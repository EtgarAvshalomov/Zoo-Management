package file_connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import roles_classes.Manager;
import zoo_classes.Animal;
import zoo_classes.Cage;

public abstract class ManagerFileManager {

    // Static list to store managers
    private static List<Manager> managers = new ArrayList<>();
    public static String currentManagerId = "";

    // return manager by id
    public static Manager getManagerById(String id) {
        for (Manager manager : managers) {
            if (manager.getId().equals(id)) {
                return manager;
            }
        }
        return null;
    }

    // Add a new manager to the list
    public static void addManager(Manager manager) {
        managers.add(manager);
    }
     // Remove a manager from the system
    public static void removeManager(Manager manager) {
        managers.remove(manager);
    }

    // Static method to load managers from a file
    public static void loadManagersFromFile(String filePath) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format for parsing dates

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                addManagerFromLine(line, dateFormat); // Parse and add each manager
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Static helper method to parse a line and add the manager to the list
    private static void addManagerFromLine(String line, SimpleDateFormat dateFormat) {
        String[] fields = line.split(","); // Split the line by commas
        if (fields.length == 7) { // Ensure the line has exactly 8 fields
            try {
                String id = fields[0];
                String fName = fields[1];
                String lName = fields[2];
                Date bDate = dateFormat.parse(fields[3]); // Convert date
                float salary = Float.parseFloat(fields[4]); // Convert salary
                float seniority = Float.parseFloat(fields[5]); // Convert seniority
                float budget = Float.parseFloat(fields[6]); // Convert budget

                // Create and add the Manager object to the list
                managers.add(new Manager(id, fName, lName, bDate, salary, seniority, budget));
            } catch (ParseException e) {
                System.err.println("Invalid date format in line: " + line);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format in line: " + line);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid enum value in line: " + line);
            }
        } else {
            System.err.println("Invalid line format: " + line);
        }
    }

    // Static method to retrieve the list of managers
    public static List<Manager> getManagers() {
        return managers;
    }

    // Static method to write the list of managers to file
    public static void writeManagersToFile(String filePath) throws Exception {
        // Delete the file before writing
        if (!FileManager.deleteFile(filePath)) {
            System.err.println("Failed to delete the existing file. Aborting write operation.");
            return; // Exit if the file could not be deleted
        }
    
        // Write only Manager objects to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Manager manager : managers) {
                writer.write(manager.getId() + "," +
                             manager.getF_name() + "," +
                             manager.getL_name() + "," +
                             new SimpleDateFormat("yyyy-MM-dd").format(manager.getB_date()) + "," +
                             manager.getSalary() + "," +
                             manager.getSeniority() + "," +
                             manager.getBudget());
                writer.newLine();
            }
        } catch (Exception e) {
            throw e;
        }
    }
    // Static mathod to Add Cage to the system
    public static void addCageToSystem(Cage cage) {
        CageFileManager.addCage(cage); // call to CageFileManager
    }
     // Static mathod to remove cage from the system
    public static void removeCageFromSystem(Cage cage) {
        CageFileManager.removeCage(cage); // call to CageFileManager
    }
     // Static mathod to Add animal to the system
    public static void addAnimalToSystem(Animal animal) {
        AnimalFileManager.addAnimal(animal); // call to AnimalFileManager
    }
     // Static mathod to remove animal form the system
    public static void removeAnimalFromSystem(Animal animal) {
        AnimalFileManager.removeAnimal(animal); // call to AnimalFileManager
    }

}