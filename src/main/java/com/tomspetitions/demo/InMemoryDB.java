package com.tomspetitions.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InMemoryDB {
    private List<Petition> petitions;
    private int nextId;

    public InMemoryDB(){
        petitions = new ArrayList<>();
        nextId = 0;
    }

    public void initializeTestData(){
        addNewPetition(new Petition(nextId, "Stop whaling", "Petition to stop the hunting of whales of the coasts of Ireland", Arrays.asList("John Macdaugh","Steven Foxe","Jill Galligen","Jeff Hexafor") ));
        addNewPetition(new Petition(nextId, "Sanction Factory Waste", "Petition to sanction the parent companies of industrial complexes that are dumping waste in fictional bay", Arrays.asList("") ));
    }
    
    public Optional<Petition> findById(int Id){
        if(Id<0){throw new IllegalArgumentException("Cannot search DB with a negative Id.");}
        return Optional.ofNullable(petitions.get(Id));   
    }

    public void addNewPetition(Petition newPetition){
        petitions.add(nextId, newPetition);
        nextId += nextId;
    }
    public void deletePetition(int Id){
        Optional<Petition> petition = findById(Id);
        if(petition.isPresent()){petitions.remove(Id);}
    }

    public Petition updatePetition(int Id, Optional<String> title, Optional<String> description, Optional<List<String>> signatures){
        Optional<Petition> oldPetition = findById(Id);
        if(oldPetition.isEmpty()){
            throw new IllegalArgumentException("No petition with the id: "+Id+" exists in the db.");
        }else{
            Petition newPetition = oldPetition.get();
            if(title.isPresent()){newPetition.setTitle(title.get());}
            if(description.isPresent()){newPetition.setDescription(description.get());}
            if(signatures.isPresent()){newPetition.setSignatures(signatures.get());}
            return newPetition;
        }
    }

}
