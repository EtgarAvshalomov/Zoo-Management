package zoo_classes;

import enums.CreatureType;
import enums.SpeciesType;
import java.util.UUID;
// Cage Class
public class Cage {
    private UUID id;
    private int maxCapacity;
    private CreatureType type;
    private SpeciesType species;
    private boolean is_clean = true;
     // Class Constructor
    public Cage(SpeciesType species, CreatureType type, int maxCapacity) {
        this.id = UUID.randomUUID();
        this.maxCapacity = maxCapacity;
        this.type = type;
        this.species = species;
    }
     // Class Constructor
    public Cage(UUID id, SpeciesType species, CreatureType type, int maxCapacity, boolean is_clean) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.type = type;
        this.species = species;
    }
    // Public Mathod to Get the Max Capacity of the cage 
    public int getMaxCapacity(){
        return this.maxCapacity;
    }
    // Public Mathod to Get the Creature Type the cage holds
    public CreatureType getType(){
        return this.type;
    }
    // Public Mathod to Get the Species Type the cage holds
    public SpeciesType getSpecies(){
        return this.species;
    }
    // Public Mathod to Get the ID of the cage
    public UUID getId(){
        return this.id;
    }
    // Public Mathod to GET if the cage is clean
    public boolean getIsClean(){
        return this.is_clean;
    }
    // Public Mathod to SET the cage clean
    public void setIsClean(boolean is_clean){
        this.is_clean = is_clean;
    }

    @Override
    public String toString() {
        return "Cage{" +
                "id=" + id +
                ", species=" + species +
                ", type=" + type +
                ", maxCapacity=" + maxCapacity +
                '}';
    }
}
