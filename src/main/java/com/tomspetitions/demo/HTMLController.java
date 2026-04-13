package com.tomspetitions.demo;


import java.util.ArrayList;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HTMLController {
    
    private InMemoryDB inMemoryDB = new InMemoryDB();
    private HTMLStorage htmlStorage = new HTMLStorage();

    public HTMLController(){
        inMemoryDB.initializeTestData();
    }

    @RequestMapping("/")
    public String displayHomePage(){
        return htmlStorage.homePage();
    }

    @RequestMapping("/viewAll")
    public String getAllPetitions(){
        return htmlStorage.viewPetitionsPage(inMemoryDB.findAll());
    }

    @RequestMapping("/sign/{id}")
    public String signPetition(@PathVariable int id){
        return htmlStorage.signPetitionPage(inMemoryDB.findById(id).get());
    }

    @RequestMapping("/new")
    public String createNewPetition(){
        return htmlStorage.createPetitionPage();
    }

    @RequestMapping("/searchPetition")
    public String searchPage(){
        return htmlStorage.searchPetitionPage();
    }

    @GetMapping("/searchAction")
    public String searchAction(@RequestParam String title){
        if(title.isEmpty() || title.length()<3){
            return htmlStorage.notificationPage("Error: Invalid search term, has to have length gretter than 3.");
        }
        //System.out.println("withing search action.\n"+inMemoryDB.findByString(title).get(0).getTitle());
        return htmlStorage.viewPetitionsPage(inMemoryDB.findByString(title));
    }

    @PostMapping("/signaction/{id}")
    public String signPetitionAction(@PathVariable int id, @RequestParam String signature, @RequestParam String email){
        Petition petitionToUpdate = inMemoryDB.findById(id).get();
        ArrayList<String> updatedSignatures = petitionToUpdate.getSignatures();
        updatedSignatures.add(signature);
        //System.out.println(updatedSignatures);
        ArrayList<String> updatedEmails = petitionToUpdate.getEmails();
        updatedEmails.add(email);
        //System.out.println(updatedEmails);
        inMemoryDB.updatePetition(id,Optional.ofNullable(null),Optional.ofNullable(null),Optional.ofNullable(updatedSignatures),Optional.ofNullable(updatedEmails));
        
        return htmlStorage.viewPetitionsPage(inMemoryDB.findAll());
    }

    @GetMapping("/delete/{id}")
    public String deletePetitionAction(@PathVariable int id){
        inMemoryDB.deletePetition(id);
        return htmlStorage.viewPetitionsPage(inMemoryDB.findAll());
    }

    @PostMapping(value="/newresult")
    public String createNewPetitionAction(@RequestParam String title, @RequestParam String description){
        inMemoryDB.addNewPetition(title, description);
        return htmlStorage.viewPetitionsPage(inMemoryDB.findAll());
    }

    // Added this function to allow the test cases to check if the DB was changing correctly in response to 
    // the html requests.
    public InMemoryDB getInMemoryDB(){
        return inMemoryDB;
    }
}
