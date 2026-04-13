package com.tomspetitions.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class InMemoryDBTests {

    
    private InMemoryDB inMemoryDB;

    @BeforeEach
    void setUp(){
        inMemoryDB = new InMemoryDB();
        inMemoryDB.initializeTestData();
    }

    @Test
    @DisplayName("Testing if a petition can be created sucessfully.")
    void createPetitionTest(){
        int initialCount = inMemoryDB.findAll().size();
        System.out.println(initialCount);

        inMemoryDB.addNewPetition("A new Petition", "The description for a new petition.");

        assertThat(inMemoryDB.findAll().size()).isEqualTo(initialCount+1);
        assertThat(inMemoryDB.findById(initialCount).get().getTitle()).isEqualTo("A new Petition");
        assertThat(inMemoryDB.findById(initialCount).get().getDescription()).isEqualTo("The description for a new petition.");
    }

    @Test
    @DisplayName("Testing if the parameters of a petition can be updated individually and seperately.")
    void updateAPetitionTest(){

        int petitionId=inMemoryDB.findAll().size();
        inMemoryDB.addNewPetition("Title to be Overridden", "Description to be Overridden");
        Petition petition = inMemoryDB.findById(petitionId).get();

        Optional<String> nullOptional1 = Optional.ofNullable(null);
        Optional<ArrayList<String>> nullOptional2 = Optional.ofNullable(null);
        Optional<String> titleOptional = Optional.ofNullable("new Title");
        Optional<String> descriptionOptional = Optional.ofNullable("new Description");
        Optional<ArrayList<String>> signaturesOptional = Optional.ofNullable( new ArrayList<>(Arrays.asList("Signature 1", "Signature 2","Signature 3")));
        Optional<ArrayList<String>> emailsOptional = Optional.ofNullable( new ArrayList<>(Arrays.asList("Email 1", "Email 2", "Email 3")));

        // Test updating 2 parameters at a time, the description and signatures parameters in this case
        inMemoryDB.updatePetition(petitionId, nullOptional1, descriptionOptional, signaturesOptional, nullOptional2);
        // Verify the title has remained the same
        assertThat(petition.getTitle()).isEqualTo("Title to be Overridden");
        // Verify that the description and signatures correctly updated
        assertThat(petition.getDescription()).isEqualTo(descriptionOptional.get());
        assertThat(petition.getSignatures()).isEqualTo(signaturesOptional.get());
        // Verify that Emails has no values at this time
        assertThat(petition.getEmails()).isEqualTo(new ArrayList());

        // Update only the emails parameter, pass the rest of the arguments as null optionals
        inMemoryDB.updatePetition(petitionId, nullOptional1, nullOptional1, nullOptional2, emailsOptional);
        // Verify the first 3 parameters hasn't changed
        assertThat(petition.getTitle()).isEqualTo("Title to be Overridden");
        assertThat(petition.getDescription()).isEqualTo(descriptionOptional.get());
        assertThat(petition.getSignatures()).isEqualTo(signaturesOptional.get());
        // verify that emails have been correctly updated
        assertThat(petition.getEmails()).isEqualTo(emailsOptional.get());

        // Check that the correct exception and mesage is returned when an update is called on a petition that doesn't exist
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> inMemoryDB.updatePetition(200, nullOptional1, nullOptional1, nullOptional2, nullOptional2));
        assertThat(ex.getMessage()).isEqualTo("No petition with Id: 200, valid range = (0-"+(inMemoryDB.findAll().size()-1)+")");

    }

    @Test
    @DisplayName("Testing if a valid deletion works and tests if an invalid deletion returns the correct IllegalArgumentException")
    void deletPetition() throws IllegalArgumentException{
        // Get the count for total number of petitions
        int initalCount = inMemoryDB.findAll().size();
        // Get the first petition in the storage
        Petition firstPetition = inMemoryDB.findById(0).get();
        // delete the first petition in the storage
        inMemoryDB.deletePetition(0);

        // Check that the total petitions in storage has been reduced by 1
        assertThat(inMemoryDB.findAll().size()).isEqualTo(initalCount-1);
        // Check that the first is nolonger the same as when we started the test.
        assertThat(inMemoryDB.findById(0).get()).isNotEqualTo(firstPetition);
        // Check that the id's stored in the other petitions are being updated correctly
        assertThat(inMemoryDB.findById(0).get().getId()).isEqualTo(0);
        

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> inMemoryDB.deletePetition(-999));
        assertThat(ex.getMessage()).isEqualTo("No petition with Id: -999, valid range = (0-"+(inMemoryDB.findAll().size()-1)+")");

        ex = assertThrows(
            IllegalArgumentException.class, 
            () -> inMemoryDB.deletePetition(20));
        assertThat(ex.getMessage()).isEqualTo("No petition with Id: 20, valid range = (0-"+(inMemoryDB.findAll().size()-1)+")");
    }

    @Test
    @DisplayName("Testing if the findBySubString() function works correctly")
    void findByTest(){
        // Define some random strings to find
        String toFind1 = "jjsklfjsjdfh";
        String toFind2 = "8f192h2hf7";
        // Create a substring of one of our search strings
        String subToFind2 = "192h2h";

        // Create new petitions to test search function.
        // In total we expect to find 3 petitions with "toFind1" and 2 petitions with "toFind2"
        // We also expect to find 4 petitions with "subToFind2", the two with "toFind2" and the 2 others with "subToFind2"
        inMemoryDB.addNewPetition(toFind1, "Desc"+subToFind2+"ion");
        inMemoryDB.addNewPetition("Title", toFind1);
        inMemoryDB.addNewPetition("8sdfjkjs"+toFind2+"712nkjdf", "");
        inMemoryDB.addNewPetition(toFind1, subToFind2);
        inMemoryDB.addNewPetition("Title", toFind2);

        assertThat(inMemoryDB.findByString(toFind1).size()).isEqualTo(3);
        assertThat(inMemoryDB.findByString(toFind2).size()).isEqualTo(2);
        assertThat(inMemoryDB.findByString(subToFind2).size()).isEqualTo(4);

    }
}