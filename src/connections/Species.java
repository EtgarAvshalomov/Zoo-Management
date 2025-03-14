package connections;

import enums.SpeciesType;
import java.util.*;

// Species Class to hold species and a manually assigned number
public class Species {
    public static final Map<SpeciesType, Species> instances = new HashMap<>();
    private final SpeciesType type;
    private final int time;
    private float h_weight;
    private float l_weight;

    private Species(SpeciesType type, int time) {
        this.type = type;
        this.time = time;
    }

    public static Species getInstance(SpeciesType type, int time) {
        return instances.computeIfAbsent(type, t -> new Species(t, time));
    }

    public SpeciesType getType() {
        return type;
    }

    public int getTime() {
        return time;
    }

    public float getH_weight() {
        return h_weight;
    }

    public float getL_weight() {
        return l_weight;
    }

    // Method to get the cleaning time for a specific species
    public static int getCleaningTime(SpeciesType type) {
        Species species = instances.get(type);  // ERROR
        
        if (species != null) {
            return species.getTime();
        } else {
            throw new IllegalArgumentException("Species not found: " + type);
        }
    }

    public static float getH_weight(SpeciesType type) {
        Species species = instances.get(type);
        if (species != null) {
            return species.getH_weight();
        } else {
            throw new IllegalArgumentException("Species not found: " + type);
        }
    }

    public static float getL_weight(SpeciesType type) {
        Species species = instances.get(type);
        if (species != null) {
            return species.getL_weight();
        } else {
            throw new IllegalArgumentException("Species not found: " + type);
        }
    }

    // Method to manually initialize species data
    public static void initializeSpeciesData() {
        // Flying Creatures
        getInstance(SpeciesType.EAGLE, 10);
        getInstance(SpeciesType.PARROT, 12);
        getInstance(SpeciesType.OWL, 15);
        getInstance(SpeciesType.DRAGON, 18);

        // Land Creatures
        getInstance(SpeciesType.LION, 20);
        getInstance(SpeciesType.TIGER, 22);
        getInstance(SpeciesType.ELEPHANT, 25);
        getInstance(SpeciesType.ZEBRA, 28);
        getInstance(SpeciesType.PANDA, 30);
        getInstance(SpeciesType.UNICORN, 35);

        // Sea Creatures
        getInstance(SpeciesType.SHARK, 40);
        getInstance(SpeciesType.PENGUIN, 42);
        getInstance(SpeciesType.FISH, 45);
        getInstance(SpeciesType.TURTLE, 48);
        getInstance(SpeciesType.MERMAID, 50);
    }
}
