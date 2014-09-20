package de.oszimt.concept.impl;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.util.Util;

import javafx.collections.ObservableList;

import java.util.List;

public class Concept implements IConcept {


    private String title = "Oszimt Projekt Benutzerverwaltung";
    private IPersistance database;

    public Concept(IPersistance db){
        this.database = db;
    }
    @Override
    public String getTitle(){
        return this.title;
    }

    @Override
    public boolean deleteUser(User user){
        this.database.deleteUser(user);
        return true;
    }

    @Override
    public boolean createUser(User user){
        this.database.createUser(user);
        return true;
    }

    @Override
    public boolean upsertUser(User user){
        this.database.upsertUser(user);
        return true;
    }

    @Override
    public void createRandomUsers(boolean useRest){
       String[] departments = new String[]{
                "Geschäftsführung","Entwicklung","Marketing",
                "Rechungsabteilung", "Kundenservice", "Verkauf",
                "Logistik","Lager", "Fahrer","Projektmanagement"
        };



        for(String dep : departments){
            this.database.createDepartment(new Department(dep));
        }

        ObservableList<User> customerList = Util.createCustomers(useRest, this.database.getAllDepartments());
        for(User user : customerList){
            this.createUser(user);
        }
    }

    @Override
    public List<Department> getAllDepartments(){
        return this.database.getAllDepartments();
    }

    @Override
    public List<User> getAllUser(){
        return this.database.getAllUser();
    }

    //TODO implement method
    @Override
    public User getUser(int id) {
        return null;
    }

}
