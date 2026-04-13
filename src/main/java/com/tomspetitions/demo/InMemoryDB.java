package com.tomspetitions.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class InMemoryDB {
    private ArrayList<Petition> petitions;

    public InMemoryDB(){
        petitions = new ArrayList<>();
    }

    // Used to populate our storage with some data
    public void initializeTestData(){
        // First we create a petition
        addNewPetition( "Stop whaling", "Petition to stop the hunting of whales of the coasts of Ireland");
        // Then we update it to add the signatures
        updatePetition(petitions.size()-1, Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable( new ArrayList<>(Arrays.asList("John Macdaugh","Steven Foxe","Jill Galligen","Jeff Hexafor"))), Optional.ofNullable(new ArrayList<>(Arrays.asList("J.Macdaugh@gmail.com","S.Foxe@hotmail.com","J.Galligen@gmail.com","J.Hexafor@hotmail.com"))));
        
        addNewPetition("Sanction Factory Waste", "Petition to sanction the parent companies of industrial complexes that are dumping waste in fictional bay");
    }
    
    // Return all petitions
    public ArrayList<Petition> findAll(){
        return petitions;
    }

    // Retrieves the petition at position "Id" within the storage array
    public Optional<Petition> findById(int Id){
        if(Id<0 || Id>=petitions.size()){throw new IllegalArgumentException("No petition with Id: "+Id+", valid range = (0-"+(petitions.size()-1)+")");}
        return Optional.ofNullable(petitions.get(Id));   
    }

    public ArrayList<Petition> findByString(String subString){
        ArrayList<Petition> tempPetitions = new ArrayList<>();

        for (Petition petition : petitions) {
            if(petition.getTitle().toLowerCase().contains(subString.toLowerCase()) || petition.getDescription().toLowerCase().contains(subString.toLowerCase())){
                tempPetitions.add(petition);
            }
        }
        return tempPetitions;
    }

    public void addNewPetition(String title, String description){
        Petition newPetition = new Petition(petitions.size(),title,description,new ArrayList<String>(),new ArrayList<String>());
        petitions.add(newPetition);
    }

    // Function to delete a petition located at the index given by "Id"
    public void deletePetition(int Id){
        Optional<Petition> petition = findById(Id);
        if(petition.isPresent()){petitions.remove(Id);}
        else{ throw new IllegalArgumentException("There is no petition in the DB with an id = "+Id);}

        // This section is a fix for a fundamental mistake I made early in dev.
        // I was treating the storage like a proper DB where Id's are'nt reused in the case of deletion then creation
        // However since I'm accessing the Arraylist storage based on their id I need to keep the coupling of petitions Id's and their position in the array.
        for(Petition adjustPetition : petitions){
            if(adjustPetition.getId()>=Id){
                adjustPetition.setId(adjustPetition.getId()-1);
            }
        }
    }

    // Function that replaces each non null paramater provided in the petition at the given Id
    public Petition updatePetition(int Id, Optional<String> title, Optional<String> description, Optional<ArrayList<String>> signatures, Optional<ArrayList<String>> emails){
        
        Petition newPetition = findById(Id).get();
        if(title.isPresent()){newPetition.setTitle(title.get());}
        if(description.isPresent()){newPetition.setDescription(description.get());}
        if(signatures.isPresent()){newPetition.setSignatures(signatures.get());}
        if(emails.isPresent()){newPetition.setEmails(emails.get());}
        return newPetition;
        
    }

}
