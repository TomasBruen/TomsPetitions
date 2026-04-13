package com.tomspetitions.demo;

import java.util.ArrayList;

public class HTMLStorage {
    
    public HTMLStorage(){}

    // Function that returns a string that contains the html for our home page
    public String homePage(String contextPath){
        String myHTML = """
            <html>
            <head>""";
        myHTML += "<link rel='stylesheet' href='"+contextPath+"/myStyle.css'>";
        myHTML+="""
            </head>
            <body class='bodyColor'>
        """;
        myHTML += navBar(contextPath);            
        myHTML +="""
            <h1 class="homeGreetText"> Welcome to Tom's petition project</h1>
            </body>
            </html>
        """;
        return myHTML;
    }

    // Returns the html page for inputting and submitting a new petition
    public String createPetitionPage(String contextPath){
        String myHTML = """
            <html>
            <head>""";
        myHTML += "<link rel='stylesheet' href='"+contextPath+"/myStyle.css'>";
        myHTML+="""
            </head>
            <body class='bodyColor'>
            """;
        myHTML += navBar(contextPath);            
        myHTML +=String.format("""
                <form action="%s/newresult" method="post" class="myColumnContainer">
                    <input name="title" value="Title" class="descriptionBox">
                    <input name="description" value="description" class="descriptionBox">
                    <button class="updateButton" type="submit"> Submit </button>
                </form>
            </body>
            </html>
        """,contextPath);
        return myHTML;
    }

    // Returns the html page containing the form used to search for specific petitions
    public String searchPetitionPage(String contextPath){
        String myHTML = """
            <html>
            <head>""";
        myHTML += "<link rel='stylesheet' href='"+contextPath+"/myStyle.css'>";
        myHTML+="""
            </head>
            <body class='bodyColor'>
            """;
        myHTML += navBar(contextPath);                    
        myHTML +=String.format("""
                <form action="%s/searchAction" method="get" class = "myColumnContainer">
                    <input name="title" value="Search Term" class="descriptionBox">
                    <button class="updateButton" type="submit"> Submit </button>
                </form>
            </body>
            </html>    
        """,contextPath);

        return myHTML;
    }

    // This returns the display page for petitions. It takes in a list of petitions and creates rows in a table for each petition passed.
    public String viewPetitionsPage(ArrayList<Petition> petitions, String contextPath){
        
        String myHTML = """
            <html>
            <head>""";
        myHTML += "<link rel='stylesheet' href='"+contextPath+"/myStyle.css'>";
        myHTML+="""
            </head>
            <body class='bodyColor'>
        """;
        myHTML += navBar(contextPath);                    
        myHTML +="""
                <table class='tableStyle'>
                    <tr>
                        <th class="titleCol">Title</th>
                        <th class="descriptionCol">Description</th>
                        <th class="listCol">Signatures</th>
                        <th class="listCol">Emails</th>
                        <th class="buttonCol"></th>
                        <th class="buttonCol"></th>
                    </tr>
        """;
        for (Petition petition : petitions){
            
            String signatureString = listStringArray(petition.getSignatures());
            String emailString = listStringArray(petition.getEmails());
            
            myHTML += String.format("""
                <tr>
                    <td class="titleCol"> %s </td>
                    <td class="descriptionCol"> %s </td>
                    <td class="listCol"> %s </td>
                    <td class="listCol"> %s </td>
                    <td class="buttonCol"> <a href='%s/sign/%d'><button class="updateButton">Sign</button></a> </td>
                    <td class="buttonCol"><a href='%s/delete/%d'><button class="deleteButton">Delete</button></a> </td>
                </tr>        
            """,petition.getTitle(),petition.getDescription(),signatureString,emailString,contextPath,petition.getId(),contextPath,petition.getId());
        }
                            
        myHTML +="""
                </table>
            </body>
            </html>    
        """;

        return myHTML;
    }

    // This function displays a single petition with form inputs to add a new signature and email.
    public String signPetitionPage(Petition petition, String contextPath){
        String myHTML = """
            <html>
            <head>""";
        myHTML += "<link rel='stylesheet' href='"+contextPath+"/myStyle.css'>";
        myHTML+="""
            </head>
            <body class='bodyColor'>
        """;
        myHTML += navBar(contextPath);                    
        myHTML += String.format(""" 
            <div class='myColumnContainer'>
                <h1 style="margin:0;">Title</h1>
                <h3 class="titleBox">%s</h3><br>
                <h1 style="margin:0;">Description</h1>
                <p class="descriptionBox">%s</p><br>
                <form action="%s/signaction/%d" method="post" class="myColumnContainer">
                    <input name="signature" value="Signature" class="signatureForm">
                    <input name="email" value="name@webpage.ie" type="email" class="emailForm">
                    <button class="updateButton" type="submit" class="updateButton"> Submit </button>
                </form>
            </div>
            </body>
            </html>
        """,petition.getTitle(),petition.getDescription(),contextPath,petition.getId());
        
        return myHTML;
    }

    // This page is simply used to display any notification passed to it as a string
    public String notificationPage(String notification, String contextPath){
        String myHTML = """
            <html>
            <head>""";
        myHTML += "<link rel='stylesheet' href='"+contextPath+"/myStyle.css'>";
        myHTML+="""
            </head>
            <body class='bodyColor'>
        """;
        myHTML += navBar(contextPath);            
        myHTML += String.format("""
                <h3>%s</h3>
            </body>
            </html>
        """, notification);
        return myHTML;
    }

    // This function returns the html that defines the navigation bar used in all other pages
    public String navBar(String contextPath){
        String myHTML = String.format("""
            <div class='myRowContainer'>
                <a href="%s/new">
                    <button class="navButton">Create Petition</button>
                </a>
                <a href="%s/viewAll">
                    <button class="navButton">View All Petitions</button>
                </a>
                <a href="%s/searchPetition">
                    <button class="navButton">Search Petitions</button>
                </a>
            </div>    
        """,contextPath,contextPath,contextPath);
        return myHTML;
    }

    // This function is used to format lists of strings for display in tables.
    // It is used on the signatures and emails parameter of petitions when displayed in tables
    public String listStringArray(ArrayList<String> list){
        String myHTML="";
        int count = 0;
        for (String item : list){
            count+=1;
            myHTML+=item;
            if(!(count==list.size())){
                myHTML+=",<br>";
            }
        }
        return myHTML;
    }
}
