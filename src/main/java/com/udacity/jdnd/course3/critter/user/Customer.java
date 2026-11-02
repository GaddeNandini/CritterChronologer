package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends Users {
    private String notes;

    @OneToMany(mappedBy = "owner")
    private List<Pet> pets = new ArrayList<>();

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<Pet> getPets() { return pets; }
    public void setPets(List<Pet> pets) { this.pets = pets; }
}
