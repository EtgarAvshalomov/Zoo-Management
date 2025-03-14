package file_connection;

import enums.SpeciesType;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import zoo_classes.Animal;

public class AnimalFileManager {
    private static List<Animal> animals = new ArrayList<>();

    // Static method to load animals from a file
    public static void loadAnimalsFromFile(String filePath) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                addAnimalFromLine(line, dateTimeFormatter);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Helper method to parse a line and add an animal to the list
    private static void addAnimalFromLine(String line, DateTimeFormatter dateTimeFormatter) {
        String[] fields = line.split(",");
        if (fields.length == 5) {
            try {
                UUID id = UUID.fromString(fields[0]);
                String name = fields[1];
                float weight = Float.parseFloat(fields[2]);
                SpeciesType species = SpeciesType.valueOf(fields[3]);
                LocalDateTime dateOfBirth = LocalDateTime.parse(fields[4], dateTimeFormatter);

                animals.add(new Animal(id, name, weight, species, dateOfBirth));
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            }
        } else {
            System.err.println("Invalid line format: " + line);
        }
    }

    // Static method to retrieve the list of animals
    public static List<Animal> getAnimals() {
        return animals;
    }

    // Static method to write the list of animals to a file
    public static void writeAnimalsToFile(String filePath) throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (!FileManager.deleteFile(filePath)) {
            System.err.println("Failed to delete the existing file. Aborting write operation.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Animal animal : animals) {
                writer.write(animal.getId() + "," +
                        animal.getName() + "," +
                        animal.getWeight() + "," +
                        animal.getSpecies() + "," +
                        animal.getDateOfBirth().format(dateTimeFormatter));
                writer.newLine();
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    // Static methods to add and remove animals
    public static void addAnimal(Animal animal) {
        animals.add(animal);
    }

    // Static methods to add and remove animals
    public static void removeAnimal(Animal animal) {
        animals.remove(animal);
    }
}
