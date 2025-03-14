package threads;

import file_connection.AnimalFileManager;
import file_connection.CageFileManager;
import file_connection.CleaningFileManager;
import file_connection.EmployeesFileManager;
import file_connection.ManagerFileManager;
import file_connection.ResidenceFileManager;

public class FileWriteThread implements Runnable {

    @Override
    public void run() {
        while (true) {
            
            // Writing to the files every 3 seconds
            // If there is an error writing to one of the files, it will continue to write to the rest of the files.

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                System.err.println(e.getStackTrace());
            }
            
            try {
                AnimalFileManager.writeAnimalsToFile("src/text_files/animals.txt");
            } catch (Exception e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

            try {
                CageFileManager.writeCagesToFile("src/text_files/cages.txt");
            } catch (Exception e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

            try {
                CleaningFileManager.writeCleaningsToFile("src/text_files/cleanings.txt");
            } catch (Exception e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

            try {
                EmployeesFileManager.writeEmployeesToFile("src/text_files/employees.txt");
            } catch (Exception e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

            try {
                ManagerFileManager.writeManagersToFile("src/text_files/managers.txt");
            } catch (Exception e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

            try {
                ResidenceFileManager.writeResidencesToFile("src/text_files/residences.txt");
            } catch (Exception e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
    }
}
