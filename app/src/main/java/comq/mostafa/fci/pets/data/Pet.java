package comq.mostafa.fci.pets.data;

import java.io.Serializable;

public class Pet implements Serializable {
    private int  id;
    private String name;
    private String breed;
    private int gender;
    private int weight;

    public Pet(int id, String name, String breed, int gender, int weight) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public int getGender() {
        return gender;
    }

    public int getWeight() {
        return weight;
    }
}
