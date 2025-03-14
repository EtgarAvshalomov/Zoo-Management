package connections;

import java.time.LocalDateTime;
import java.util.UUID;

public class Residance {
    private UUID animal_id;
    private UUID cage_id;
    private LocalDateTime start_time;
    private LocalDateTime end_time = null;

    public Residance(UUID animal, UUID cage) {
        this.animal_id = animal;
        this.cage_id = cage;
        this.start_time = LocalDateTime.now();
    }

    public Residance(UUID animal, UUID cage, LocalDateTime start, LocalDateTime end) {
        this.animal_id = animal;
        this.cage_id = cage;
        this.start_time = start;
        this.end_time = end;
    }

    public void endResidance() { this.end_time = LocalDateTime.now(); }

    public UUID getAnimalId() { return this.animal_id; }

    public UUID getCageId() { return this.cage_id; }

    public LocalDateTime getStartTime() { return this.start_time; }

    public LocalDateTime getEndTime() { return this.end_time; }

    @Override
    public String toString() {
        return "Residance{" +
                "animalId=" + animal_id +
                ",cageId=" + cage_id +
                ",startTime=" + start_time +
                ",endTime=" + (end_time != null ? end_time : "Ongoing") +
                '}';
    }
}
