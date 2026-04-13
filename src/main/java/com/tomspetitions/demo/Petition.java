package com.tomspetitions.demo;

import java.util.ArrayList;

public class Petition {
    private int Id;
    private String title, description;
    private ArrayList<String> signatures, emails;

    public Petition(int Id, String title, String description, ArrayList<String> signatures, ArrayList<String> emails){
        this.Id = Id;
        this.title = title;
        this.description = description;
        this.signatures = signatures;
        this.emails = emails;
    }

    public int getId(){
        return Id;
    }
    public void setId(int Id){
        this.Id=Id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public ArrayList<String> getSignatures(){
        return signatures;
    }
    public void setSignatures(ArrayList<String> signatures){
        this.signatures = signatures;
    }

    public ArrayList<String> getEmails(){
        return emails;
    }
    public void setEmails(ArrayList<String> emails){
        this.emails = emails;
    }
}
