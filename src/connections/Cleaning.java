package connections;

import java.time.LocalDateTime;
import java.util.UUID;

// Cleaning class
public class Cleaning {
    private UUID cage_id;
    private String employee_id;
    private LocalDateTime start_time;
    private LocalDateTime end_time = null;

    public Cleaning(UUID cage, String employee) {
        this.cage_id = cage;
        this.employee_id = employee;
        this.start_time = LocalDateTime.now();
    }

    public Cleaning(UUID cage, String employee, LocalDateTime start, LocalDateTime end) {
        this.cage_id = cage;
        this.employee_id = employee;
        this.start_time = start;
        this.end_time = end;
    }

    public void endCleaning() { this.end_time = LocalDateTime.now(); }

    // Get Cage ID
    public UUID getCageId() { return this.cage_id; }
    // Get Employee ID
    public String getEmployeeId() { return this.employee_id; }
    // Get Start Time
    public LocalDateTime getStartTime() { return this.start_time; }
    // Get End time
    public LocalDateTime getEndTime() { return this.end_time; }


    public boolean isFinished() {
        return this.end_time != null;
    }

    @Override
    public String toString() {
        return "Cleaning{" +
                "cageId=" + cage_id +
                ", employeeId='" + employee_id + '\'' +
                ", startTime=" + start_time +
                ", endTime=" + (end_time != null ? end_time : "Ongoing") +
                '}';
    }
}
