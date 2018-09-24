package comq.mostafa.fci.pets.data;
import android.database.Cursor;
import android.util.Log;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import comq.mostafa.fci.pets.data.PetContract.PetEntry;

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

    public static ArrayList<Pet> getPetsList(Cursor cursor){
        ArrayList<Pet> pets = new ArrayList<>();

        int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
        int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
        int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

        while (cursor.moveToNext()){
            int currentID = cursor.getInt(idColumnIndex);
            String currentName = cursor.getString(nameColumnIndex);
            String currentBreed = cursor.getString(breedColumnIndex);
            int currentGender = cursor.getInt(genderColumnIndex);
            int currentWeight = cursor.getInt(weightColumnIndex);
            Pet pet = new Pet(currentID,currentName,currentBreed,currentGender,currentWeight);
            pets.add(pet);
        }
        return pets;
    }
}
