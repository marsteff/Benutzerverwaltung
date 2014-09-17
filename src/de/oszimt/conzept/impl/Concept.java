package de.oszimt.conzept.impl;

import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import javafx.collections.ObservableList;

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

    public boolean updateUser(User user){
        this.database.updateUser(user);
        return true;
    }

    public ObservableList<User> getAllUser(){
        return this.database.getAllUser();
    }

}
