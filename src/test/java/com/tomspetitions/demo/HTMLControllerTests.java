package com.tomspetitions.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.ServletException;

@SpringBootTest
public class HTMLControllerTests {

    private MockMvc mockMvc;

    private HTMLController htmlController = new HTMLController();

    @BeforeEach
    void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(htmlController).build();
    }

    @Test
    @DisplayName("Testing creating new petition using html requests")
    void createPetitionTest() throws Exception{
        int initalPetitionCount = htmlController.getInMemoryDB().findAll().size();

        // Make a valid html request to create a new petition
        mockMvc.perform(post("/newresult").param("title","new Petition").param("description", "test Description"));
        // Assert that the total number of petitions changed by 1 and that there is a petition present with the title "new Petition"
        assertThat(htmlController.getInMemoryDB().findAll().size()).isEqualTo(initalPetitionCount+1);
        assertThat(htmlController.getInMemoryDB().findByString("new Petition").size()).isEqualTo(1);

        // Make 2 invalid html requests to create a new petition. Each is missing one of the required parameters
        mockMvc.perform(post("/newresult").param("title","new Petition"));
        mockMvc.perform(post("/newresult").param("description", "test Description"));
        // Assert that the number of petitions hasn't changed since the first sucessful html request
        assertThat(htmlController.getInMemoryDB().findAll().size()).isEqualTo(initalPetitionCount+1);
    }

    @Test
    @DisplayName("Testing deleting petitions using html requests")
    void deletePetitionTest() throws Exception{
        int initalPetitionCount = htmlController.getInMemoryDB().findAll().size();

        // Make 2 valid deletion html requests: note our initialization of htmlController creates 2 petitions to start with
        mockMvc.perform(get("/delete/0"));
        assertThat(htmlController.getInMemoryDB().findAll().size()).isEqualTo(initalPetitionCount-1);
        // Use the same deletion request to delete the final petition,
        // This also tests the updating of petition Id's after deletion
        mockMvc.perform(get("/delete/0"));
        assertThat(htmlController.getInMemoryDB().findAll().size()).isEqualTo(initalPetitionCount-2);

        // Test an invalid deletion request
        ServletException ex = assertThrows(
            ServletException.class, 
            () -> mockMvc.perform(get("/delete/0")));
        assertThat(ex.getMessage()).isEqualTo("Request processing failed: java.lang.IllegalArgumentException: No petition with Id: 0, valid range = (0-"+(htmlController.getInMemoryDB().findAll().size()-1)+")");
    }

    @Test
    @DisplayName("Testing the signage of petitions through html requests")
    void signPetitionTest() throws Exception{
        int initialSignatureCount = htmlController.getInMemoryDB().findById(0).get().getSignatures().size();
        int initialEmailCount = htmlController.getInMemoryDB().findById(0).get().getEmails().size();

        // Test some invalid signage requests, missing a parameter each.
        mockMvc.perform(post("/signaction/0").param("email", "name@webpage.ie"));
        mockMvc.perform(post("/signaction/0").param("signature","new Signature"));
        // Ensure that the number of signatures and emails don't change
        assertThat(htmlController.getInMemoryDB().findById(0).get().getSignatures().size()).isEqualTo(initialSignatureCount);
        assertThat(htmlController.getInMemoryDB().findById(0).get().getEmails().size()).isEqualTo(initialEmailCount);

        // Test a valid signagre request
        mockMvc.perform(post("/signaction/0").param("signature","new Signature").param("email", "name@webpage.ie"));
        // Check that the number of signatures and emails has increased by 1.
        assertThat(htmlController.getInMemoryDB().findById(0).get().getSignatures().size()).isEqualTo(initialSignatureCount+1);
        assertThat(htmlController.getInMemoryDB().findById(0).get().getEmails().size()).isEqualTo(initialEmailCount+1);
    }
}
