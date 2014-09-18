package de.oszimt.conzept.impl;

import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.util.Util;
import javafx.collections.ObservableList;

import java.util.List;

public class Concept {


    private String title = "Oszimt Projekt Benutzerverwaltung";
    private IPersistance database;

    public Concept(IPersistance db){
        this.database = db;
    }
    public String getTitle(){
        return this.title;
    }

    public boolean deleteUser(User user){
        this.database.deleteUser(user);
        return true;
    }

    public boolean createUser(User user){
        this.database.createUser(user);
        return true;
    }

    public boolean upsertUser(User user){
        this.database.upsertUser(user);
        return true;
    }

    public void createRandomUsers(boolean useRest){
        ObservableList<User> customerList = Util.createCustomers(useRest);
        for(User user : customerList){
            this.createUser(user);
        }
    }


    public List<User> getAllUser(){
        return this.database.getAllUser();
    }

}
