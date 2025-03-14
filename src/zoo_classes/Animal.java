package zoo_classes;

import enums.SpeciesType;
import java.time.LocalDateTime;
import java.util.UUID;

public class Animal {
    private UUID id;
    private String name;
    private float weight;
    private SpeciesType species;
    private LocalDateTime date_of_birth;

    // Animal Class
    public Animal(String name, float weight, SpeciesType species) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.weight = weight;
        this.species = species;
        this.date_of_birth = LocalDateTime.now();
    }
     // Class Constructor
    public Animal(UUID id, String name, float weight, SpeciesType species, LocalDateTime date_of_birth) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.species = species;
        this.date_of_birth = date_of_birth;
    }
    // Public mathod to GET animal ID
    public UUID getId(){
        return this.id;
    }
    // Public mathod to SET the animal's name
    public String getName(){
        return this.name;
    }
    // Public mathod to GET the animal's weight
    public float getWeight(){
        return this.weight;
    }
    // Public mathod to GET the animal's species
    public SpeciesType getSpecies(){
        return this.species;
    }
    // Public mathod to GET the animal's BirthDay
    public LocalDateTime getDateOfBirth(){
        return this.date_of_birth;
    }


    // Override toString for easy printing
    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", species=" + species +
                ", dateOfBirth=" + date_of_birth +
                '}';
    }
}
