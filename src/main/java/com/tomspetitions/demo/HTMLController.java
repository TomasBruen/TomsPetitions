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

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class HTMLController {

    private InMemoryDB inMemoryDB = new InMemoryDB();
    private HTMLStorage htmlStorage = new HTMLStorage();

    public HTMLController(){
        inMemoryDB.initializeTestData();
    }

    @RequestMapping("/")
    public String displayHomePage(HttpServletRequest request){
        String contextPath = request.getContextPath();
        return htmlStorage.homePage(contextPath);
    }

    @RequestMapping("/viewAll")
    public String getAllPetitions(HttpServletRequest request){
        String contextPath = request.getContextPath();
        return htmlStorage.viewPetitionsPage(inMemoryDB.findAll(),contextPath);
    }

    @RequestMapping("/sign/{id}")
    public String signPetition(HttpServletRequest request,@PathVariable int id){
        String contextPath = request.getContextPath();
        return htmlStorage.signPetitionPage(inMemoryDB.findById(id).get(),contextPath);
    }

    @RequestMapping("/new")
    public String createNewPetition(HttpServletRequest request){
        String contextPath = request.getContextPath();
        return htmlStorage.createPetitionPage(contextPath);
    }

    @RequestMapping("/searchPetition")
    public String searchPage(HttpServletRequest request){
        String contextPath = request.getContextPath();
        return htmlStorage.searchPetitionPage(contextPath);
    }

    @GetMapping("/searchAction")
    public String searchAction(HttpServletRequest request,@RequestParam String title){
        String contextPath = request.getContextPath();
        if(title.isEmpty() || title.length()<3){
            return htmlStorage.notificationPage("Error: Invalid search term, has to have length gretter than 3.",contextPath);
        }
        //System.out.println("withing search action.\n"+inMemoryDB.findByString(title).get(0).getTitle());
        return htmlStorage.viewPetitionsPage(inMemoryDB.findByString(title),contextPath);
    }

    @PostMapping("/signaction/{id}")
    public String signPetitionAction(HttpServletRequest request,@PathVariable int id, @RequestParam String signature, @RequestParam String email){
        String contextPath = request.getContextPath();
        Petition petitionToUpdate = inMemoryDB.findById(id).get();
        ArrayList<String> updatedSignatures = petitionToUpdate.getSignatures();
        updatedSignatures.add(signature);
        //System.out.println(updatedSignatures);
        ArrayList<String> updatedEmails = petitionToUpdate.getEmails();
        updatedEmails.add(email);
        //System.out.println(updatedEmails);
        inMemoryDB.updatePetition(id,Optional.ofNullable(null),Optional.ofNullable(null),Optional.ofNullable(updatedSignatures),Optional.ofNullable(updatedEmails));
        
        return htmlStorage.viewPetitionsPage(inMemoryDB.findAll(),contextPath);
    }

    @GetMapping("/delete/{id}")
    public String deletePetitionAction(HttpServletRequest request,@PathVariable int id){
        String contextPath = request.getContextPath();
        inMemoryDB.deletePetition(id);
        return htmlStorage.viewPetitionsPage(inMemoryDB.findAll(),contextPath);
    }

    @PostMapping(value="/newresult")
    public String createNewPetitionAction(HttpServletRequest request,@RequestParam String title, @RequestParam String description){
        String contextPath = request.getContextPath();
        inMemoryDB.addNewPetition(title, description);
        return htmlStorage.viewPetitionsPage(inMemoryDB.findAll(),contextPath);
    }

    // Added this function to allow the test cases to check if the DB was changing correctly in response to 
    // the html requests.
    public InMemoryDB getInMemoryDB(){
        return inMemoryDB;
    }
}
