package threads;

import connections.Cleaning;
import file_connection.CageFileManager;
import file_connection.CleaningFileManager;
import file_connection.EmployeesFileManager;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import roles_classes.Employee;
import zoo_classes.Cage;
import GUI.EmployeePanel;

public class CleaningHandlingThreads implements Runnable {
    private UUID cage_id;
    private String employee_id;
    private final ExecutorService cleaningExecutor = Executors.newCachedThreadPool();

    public CleaningHandlingThreads(UUID cage_id, String employee_id) {
        this.cage_id = cage_id;
        this.employee_id = employee_id;
    }

    public void run(){
        startCleaningProcess(cage_id, employee_id);
    }

    private void startCleaningProcess(UUID cage_id, String employee_id) {
        cleaningExecutor.submit(() -> {
            try {
                Cage cage = CageFileManager.getCage(cage_id);
                Employee employee = EmployeesFileManager.getEmployeeById(employee_id);
                
                if (cage == null || employee == null) {
                    System.err.println("Error: Cage or Employee not found");
                    return;
                }

                System.out.println("Starting cleaning process for Cage " + cage_id + " with Employee " + employee_id);
                EmployeesFileManager.removeAvailableEmployee(employee);
                employee.setAvailable(false);
                
                Cleaning currentCleaning = new Cleaning(cage.getId(), employee.getId());
                CleaningFileManager.addCleaning(currentCleaning);
                
                System.out.println("Showing cleaning dialog for Employee " + employee_id);
                EmployeePanel.showCleaningDialogForEmployee(employee);
                
                System.out.println("Waiting for cleaning confirmation from Employee " + employee_id);
                employee.cleanCage(cage.getId());
                System.out.println("Employee " + employee_id + " confirmed cleaning");
                currentCleaning.endCleaning();

                if (employee.isLogged()) {
                    // Add a 5-second delay before making the employee available again
                    try {
                        System.out.println("Waiting 5 seconds before making Employee " + employee_id + " available again");
                        Thread.sleep(5000);  // 5000 milliseconds = 5 seconds
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Delay interrupted for Employee " + employee_id);
                    }

                    // Only make the employee available if they're still logged in after the delay
                    if (employee.isLogged()) {
                        System.out.println("Employee " + employee_id + " still logged in, returning to available queue");
                        employee.setAvailable(true); 
                        EmployeesFileManager.returnEmployeeToQueue(employee);
                    }
                }

                cage.setIsClean(true);
                synchronized (cage) {
                    cage.notify();
                }
                System.out.println("Cleaning process completed for Cage " + cage_id);
            } catch (Exception e) {
                System.err.println("Error in cleaning process: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
