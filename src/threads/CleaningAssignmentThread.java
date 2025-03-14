package threads;

import file_connection.CageFileManager;
import file_connection.EmployeesFileManager;
import java.util.UUID;

public class CleaningAssignmentThread implements Runnable {

    @Override
    public void run() {
        while (true) {
            waitForAvailableCageAndEmployee();
            // CageFileManager.printAllCleaningQueue();
            assignEmployeeToCage();
        }
    }

    // ממתין שיהיה כלוב ועובד זמין
    private void waitForAvailableCageAndEmployee() {
        try {
            while (CageFileManager.getCageToCleanQueue().peek() == null || EmployeesFileManager.getAvailableEmployee().peek() == null) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Waiting for available cage and employee was interrupted.");
        }
    }

    // הקצאת עובד לכלוב
    private void assignEmployeeToCage() {
        try {
            // System.out.println("==========here=============");
            UUID cageId = CageFileManager.getNextCageIdForCleaning();
            // Cage cageToClean = CageFileManager.getCage(cageId);
            // System.out.println("cageToClean: " + cageToClean);
            String employeeId = EmployeesFileManager.getNextAvailableEmployee();
            // System.out.println("employeeId: " + employeeId);

            Thread currentClean = new Thread(new CleaningHandlingThreads(cageId, employeeId));
            currentClean.start();

        } catch (Exception e) {
            System.err.println("Error assigning employee to cage: " + e.getMessage());
        }
    }
}