package file_connection;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import roles_classes.Employee;

public abstract class EmployeesFileManager {

    // Static list to store employees
    private static List<Employee> employees = new ArrayList<>();
    public static Queue<String> availableEmployees = new ConcurrentLinkedQueue<>();
    public static String currentEmployeeId = "";

    // Static method to retrieve the list of employees
    public static List<Employee> getEmployees() {
        return employees;
    }

    public static void addEmployee(Employee employee) {
        try {
            synchronized (employees) {
                employees.add(employee);
            }
        } catch (Exception e) {
            System.err.println("Error adding an employee: " + e.getMessage());
        }
    }
    // Static method to remove employee from the system
    public static void removeEmployee(Employee employee) {
        try {
            synchronized (employees) {
                employees.remove(employee);
            }
        } catch (Exception e) {
            System.err.println("Removing employee failed: " + e.getMessage());
        }
    }

    // Retrieve an employee by ID
    public static Employee getEmployeeById(String id) {
        synchronized (employees) {
            for (Employee employee : employees) {
                if (employee.getId().equals(id)) {
                    return employee;
                }
            }
        }
        return null;
    }

    // Peek at the first available employee without removing
    public static String peekAvailableEmployee() {
            return availableEmployees.peek();
        
    }
    
    // Load employees from a file
    public static void loadEmployeesFromFile(String filePath) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                addEmployeeFromLine(line, dateFormat);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Parse a line and add the employee to the list
    private static void addEmployeeFromLine(String line, SimpleDateFormat dateFormat) {
        String[] fields = line.split(",");
        if (fields.length == 6) {
            try {
                String id = fields[0];
                String fName = fields[1];
                String lName = fields[2];
                Date bDate = dateFormat.parse(fields[3]);
                float salary = Float.parseFloat(fields[4]);
                float seniority = Float.parseFloat(fields[5]);

                employees.add(new Employee(id, fName, lName, bDate, salary, seniority));
            } catch (ParseException | NumberFormatException e) {
                System.err.println("Invalid line: " + line + " -> " + e.getMessage());
            }
        } else {
            System.err.println("Invalid line format: " + line);
        }
    }

    // Write employees to a file
    public static void writeEmployeesToFile(String filePath) throws Exception {
        if (!FileManager.deleteFile(filePath)) {
            System.err.println("Failed to delete the existing file. Aborting write operation.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Employee employee : employees) {
                writer.write(employee.getId() + "," +
                             employee.getF_name() + "," +
                             employee.getL_name() + "," +
                             new SimpleDateFormat("yyyy-MM-dd").format(employee.getB_date()) + "," +
                             employee.getSalary() + "," +
                             employee.getSeniority());
                writer.newLine();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    // Fill the availableEmployees list
    public static void fillAvailableEmployees() {
        synchronized (employees) {
            for (Employee employee : employees) {
                if (employee.isAvailable() && !availableEmployees.contains(employee.getId())) {
                    availableEmployees.add(employee.getId());
                }
            }
        }
    }

    // Add an employee to the availableEmployees list
    public static void addAvailableEmployee(Employee employee) {
        synchronized (availableEmployees) {
            if (!availableEmployees.contains(employee.getId().trim())) {
                employee.setAvailable(true);
                availableEmployees.add(employee.getId().trim());
            }
        }
    }
    
    // Remove an employee from the availableEmployees list
    public static void removeAvailableEmployee(Employee employee) {
        synchronized (availableEmployees) {
            availableEmployees.remove(employee.getId());
        }
    }

    // Return an employee to the availableEmployees list
    public static void returnEmployeeToQueue(Employee employee) {
        if (employee != null && employee.isAvailable()) {
            synchronized (availableEmployees) {
                if (!availableEmployees.contains(employee.getId())) {
                    availableEmployees.add(employee.getId());
                }
            }
        }
    }

    // Getters
    public static Queue<String> getAvailableEmployee() {
        return availableEmployees;
    }

    public static String getNextAvailableEmployee() {
        return availableEmployees.poll();
    } 
}
