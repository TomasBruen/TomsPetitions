package com.tomspetitions.demo;

import java.util.List;

public class Petition {
    private int Id;
    private String title, description;
    private List<String> signatures;

    public Petition(int Id, String title, String description, List<String> signatures){
        this.Id = Id;
        this.title = title;
        this.description = description;
        this.signatures = signatures;
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

    public List<String> getSignatures(){
        return signatures;
    }
    public void setSignatures(List<String> signatures){
        this.signatures = signatures;
    }
}
