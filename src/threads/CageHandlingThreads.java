package threads;

import connections.Species;
import enums.SpeciesType;
import file_connection.CageFileManager;
import file_connection.ResidenceFileManager;
import java.util.UUID;
import zoo_classes.Cage;

public class CageHandlingThreads implements Runnable {
    private UUID id;  // this id needs to be the same as the cage id
    private volatile boolean running = true;

    // Constructor
    public CageHandlingThreads(UUID id) {
        this.id = id;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                startCleaningProcess(id);
            } catch (InterruptedException e) {
                System.out.println("Cleaning thread for cage " + id + " is shutting down.");
                running = false;
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Cleaning thread for cage " + id + " has stopped.");
    }

    // Start and re-run the cleaning process
    private void startCleaningProcess(UUID id) throws InterruptedException {
        if (id == null) {
            System.err.println("Error: Cage ID is null in startCleaningProcess.");
            return;
        }

        Cage currentCage = CageFileManager.getCage(id);
        if (currentCage == null) {
            System.out.println("Cage " + id + " has been removed, stopping cleaning process.");
            running = false;
            Thread.currentThread().interrupt();
            return;
        }

        SpeciesType species = currentCage.getSpecies();
        if (species == null) {
            System.err.println("Error: Species type is null for Cage " + id);
            return;
        }

        // Get the cleaning time and capacity for the species
        int time = Species.getCleaningTime(species);
        int capacity = ResidenceFileManager.getCapacity(id);
        
        // Prevent division by zero by using a minimum capacity of 1
        int effectiveCapacity = Math.max(1, capacity);
        Thread.sleep((1000 * time) / effectiveCapacity);

        // Check if the cage still exists before proceeding
        currentCage = CageFileManager.getCage(id);
        if (currentCage == null || !running) {
            return;
        }

        // Set the cage as dirty and add it to the cleaning queue
        currentCage.setIsClean(false);
        CageFileManager.addCageToCleanQueue(id);

        // Wait for the cleaning process to complete
        synchronized (currentCage) {
            while (!currentCage.getIsClean() && running) {
                currentCage.wait(1000);  // Wait with timeout to check running status
            }
        }

        Thread.sleep(200);  // Wait before retrying
    }
}