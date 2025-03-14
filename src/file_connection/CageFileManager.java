package file_connection;

import enums.CreatureType;
import enums.SpeciesType;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import threads.CageHandlingThreads;
import zoo_classes.Animal;
import zoo_classes.Cage;

public class CageFileManager {
    private static List<Cage> cages = new ArrayList<>();
    private static Queue<UUID> CleaningQueue = new LinkedList<>();
    private static Map<UUID, Thread> cageThreads = new HashMap<>();

    // Static method to add a cage to the list
    public static void addCage(Cage cage) {
        cages.add(cage);
        // Start cleaning thread for the new cage
        Thread cageThread = new Thread(new CageHandlingThreads(cage.getId()));
        cageThread.start();
        cageThreads.put(cage.getId(), cageThread);
        System.out.println("Started cleaning cycle for Cage " + cage.getId());
    }

    // Static method to remove a cage from the list
    public static synchronized void removeCage(Cage cage) {
        if (cage == null) {
            System.err.println("Error: Attempted to remove null cage");
            return;
        }

        // First remove the cage from the list to prevent new operations
        cages.remove(cage);

        // Remove the cage from the cleaning queue if it's there
        CleaningQueue.remove(cage.getId());

        // End all active residences for this cage
        ResidenceFileManager.removeResidencesForCage(cage.getId());

        // Remove all animals from this cage
        List<Animal> animals = AnimalFileManager.getAnimals();
        for (Animal animal : new ArrayList<>(animals)) {
            if (ResidenceFileManager.isAnimalInCage(animal.getId(), cage.getId())) {
                AnimalFileManager.removeAnimal(animal);
            }
        }

        // Finally stop the cleaning thread for this cage
        Thread cageThread = cageThreads.get(cage.getId());
        if (cageThread != null) {
            cageThread.interrupt();
            try {
                // Wait for the thread to finish, but not indefinitely
                cageThread.join(2000);
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for cage thread to stop");
            }
            cageThreads.remove(cage.getId());
        }

        System.out.println("Successfully removed cage " + cage.getId());
    }

    // Static method to retrieve the list of cages
    public static List<Cage> getCages() {
        return cages;
    }

    // Static method to retrieve a cage by ID  
    public static Cage getCage(UUID id) {
        for (Cage cage : cages) {
            if (cage.getId().equals(id)) {
                return cage;
            }
        }
        return null;
    }
    
    // Static method to add a cage to the end of the cleaning queue
    public synchronized static void addCageToCleanQueue(UUID cageId) {
        if (cageId == null) {
            System.err.println("Error: Attempted to add a null cage ID to the cleaning queue.");
            return;
        }
    
        if (CleaningQueue.contains(cageId)) {
            System.out.println("Cage " + cageId + " is already in the cleaning queue.");
            return;
        }
    
        CleaningQueue.add(cageId);
        System.out.println("Added Cage " + cageId + " to the cleaning queue.");
    }
    
    
    // Static method to retrieve the cleaning queue
    public static Queue<UUID> getCageToCleanQueue() {
        return CleaningQueue;
    }

    // Static method to retrieve the next cage ID for cleaning
    public static UUID getNextCageIdForCleaning(){
        return CleaningQueue.poll();
    }

    // Static method to initialize the cage handling threads
    public static void initializeCageHandler() {
        for (Cage cage : cages) {
            Thread cageThread = new Thread(new CageHandlingThreads(cage.getId()));
            cageThread.start();
            cageThreads.put(cage.getId(), cageThread);
            System.out.println("Started cleaning cycle for Cage " + cage.getId());
        }
    }


    // Helper method to parse a line and add a cage to the list
    private static void addCageFromLine(String line) {
        String[] fields = line.split(",");
        if (fields.length == 5) {
            try {
                UUID id = UUID.fromString(fields[0].trim());
                SpeciesType species = SpeciesType.valueOf(fields[1].trim().toUpperCase());
                CreatureType type = CreatureType.valueOf(fields[2].trim().toUpperCase());
                int maxCapacity = Integer.parseInt(fields[3].trim());
                boolean isClean = Boolean.parseBoolean(fields[4].trim());
    
                cages.add(new Cage(id, species, type, maxCapacity, isClean));
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            }
        } else {
            System.err.println("Invalid line format: " + line);
        }
    }

    public static void printAllCleaningQueue(){
        try {
            // System.out.println("Cleaning Queue: ");
            for (UUID cageId : CleaningQueue) {
                System.out.println(cageId);
            }
        } catch (Exception e) {
            System.out.println("Error printing cleaning queue: " + e.getMessage());
        }
    }
    
    // Static method to load cages from a file
    public static void loadCagesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                addCageFromLine(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Static method to write the list of cages to a file
    public static void writeCagesToFile(String filePath) throws Exception {
        // Delete the file before writing
        if (!FileManager.deleteFile(filePath)) {
            System.err.println("Failed to delete the existing file. Aborting write operation.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Cage cage : cages) {
                writer.write(cage.getId() + "," +
                cage.getSpecies() + "," +
                cage.getType() + "," +
                cage.getMaxCapacity() + "," +
                cage.getIsClean());
                writer.newLine();
            }
            
        } catch (Exception e) {
            throw e;
        }
    }
}
