package roles_classes;
import java.util.Date;
import java.util.UUID;
// Employee Class
public class Employee {
    private String id;
    private String f_name;
    private String l_name;
    private Date b_date;
    private float salary;
    private float seniority;
    private boolean isAvailable = false; 
    private boolean isLogged = false;
    private volatile boolean cleaningCompleted = false;
    // Class Constructor
    public Employee(String id, String f_name, String l_name, Date b_date, float salary, float seniority) {
        this.id = id;
        this.f_name = f_name;
        this.l_name = l_name;
        this.b_date = b_date;
        this.salary = salary;
        this.seniority = seniority;
    }
    // Public mathod to GET salary
    public float getSalary() {
        return salary;
    }
    // Public mathod to SET salary
    public void setSalary(float salary) {
        this.salary = salary;
    }
    // Public mathod to GET Seniority
    public float getSeniority() {
        return seniority;
    }
    // Public mathod to SET Seniority
    public void setSeniority(float seniority) {
        this.seniority = seniority;
    }
    // Public mathod to check if the employee is available to be assign for cleaning
    public synchronized boolean isAvailable() {
        return isAvailable;
    }
    // Public mathod to set the employee to be available for cleaning
    public synchronized void setAvailable(boolean available) {
        isAvailable = available;
    }
    // Public mathod to check if the employee is logged to the system
    public synchronized boolean isLogged() {
        return isLogged;
    }
    // Public mathod to set the employee as logged to the system 
    public synchronized void setIsLogged(boolean logged) {
        isLogged = logged;
    }
    // Public mathod to GET ID
    public String getId() {
        return this.id;
    }
    // Public mathod to GET First name
    public String getF_name() {
        return this.f_name;
    }
    // Public mathod to GET Last name
    public String getL_name() {
        return this.l_name;
    }
    // Public mathod to GET BirthDate
    public Date getB_date() {
        return this.b_date;
    }

    // Public mathod Clean Cage
    public synchronized void cleanCage(UUID cageToClean) {
        System.out.println("Employee " + this.id + " starting to clean Cage " + cageToClean);
        
        // Reset the cleaning status
        cleaningCompleted = false;
        
        // Wait for the cleaning to be completed via the UI
        while (!cleaningCompleted && isLogged) {
            try {
                System.out.println("Employee " + this.id + " waiting for cleaning confirmation for Cage " + cageToClean);
                wait(); // Wait until notified by completeCurrentCleaning
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Employee " + this.id + " was interrupted while cleaning Cage " + cageToClean);
                break;
            }
        }
        
        if (cleaningCompleted) {
            System.out.println("Employee " + this.id + " has completed cleaning Cage " + cageToClean);
        } else {
            System.out.println("Employee " + this.id + " cleaning of Cage " + cageToClean + " was cancelled (logged out)");
        }
    }

    // Method to signal that cleaning is complete (called from UI)
    public synchronized void completeCurrentCleaning() {
        System.out.println("Marking cleaning as completed for Employee " + this.id);
        this.cleaningCompleted = true;
        notifyAll(); // Notify waiting threads that cleaning is complete
    }

   @Override
   public String toString() {
       return "Employee{" +
               "id='" + id + '\'' +
               ", f_name='" + f_name + '\'' +
               ", l_name='" + l_name + '\'' +
               ", b_date=" + b_date +
               '}';
   }
}
