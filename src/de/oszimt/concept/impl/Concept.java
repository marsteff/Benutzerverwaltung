package de.oszimt.concept.impl;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.util.Util;

import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        this.database.deleteUser(user.getId());
        return true;
    }

    private User userMapToUser(Map<String,Object> userMap){
        IPersistance keys = this.database;
        return new User(
                userMap.containsKey(keys.getKeyUserFirstname()) ? userMap.get(keys.getKeyUserFirstname()).toString() : null,
                userMap.containsKey(keys.getKeyUserLastname()) ? userMap.get(keys.getKeyUserLastname()).toString() : null,
                userMap.containsKey(keys.getKeyUserBirthday()) ? (LocalDate) userMap.get(keys.getKeyUserBirthday()) : null,
                userMap.containsKey(keys.getKeyUserCity()) ? userMap.get(keys.getKeyUserCity()).toString() : null,
                userMap.containsKey(keys.getKeyUserStreet()) ? userMap.get(keys.getKeyUserStreet()).toString() : null,
                userMap.containsKey(keys.getKeyUserStreetNr()) ? userMap.get(keys.getKeyUserStreetNr()).toString() : null,
                userMap.containsKey(keys.getKeyUserZipCode()) ? (int) userMap.get(keys.getKeyUserZipCode()) : null,
                new Department(
                        (int) ((Map) userMap.get(keys.getKeyUserDepartment())).get(keys.getKeyDepartmentId()),
                        ((Map) userMap.get(keys.getKeyUserDepartment())).get(keys.getKeyDepartmentName()).toString()
                )
        );
    }

    private Map<String,Object> userToUserMap(User user){
        IPersistance keys = this.database;
        return new User(
                userMap.containsKey(keys.getKeyUserFirstname()) ? userMap.get(keys.getKeyUserFirstname()).toString() : null,
                userMap.containsKey(keys.getKeyUserLastname()) ? userMap.get(keys.getKeyUserLastname()).toString() : null,
                userMap.containsKey(keys.getKeyUserBirthday()) ? (LocalDate) userMap.get(keys.getKeyUserBirthday()) : null,
                userMap.containsKey(keys.getKeyUserCity()) ? userMap.get(keys.getKeyUserCity()).toString() : null,
                userMap.containsKey(keys.getKeyUserStreet()) ? userMap.get(keys.getKeyUserStreet()).toString() : null,
                userMap.containsKey(keys.getKeyUserStreetNr()) ? userMap.get(keys.getKeyUserStreetNr()).toString() : null,
                userMap.containsKey(keys.getKeyUserZipCode()) ? (int) userMap.get(keys.getKeyUserZipCode()) : null,
                new Department(
                        (int) ((Map) userMap.get(keys.getKeyUserDepartment())).get(keys.getKeyDepartmentId()),
                        ((Map) userMap.get(keys.getKeyUserDepartment())).get(keys.getKeyDepartmentName()).toString()
                )
        );
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

    //TODO implement method - for now only for test purposes
    @Override
    public User getUser(int id) {
        List<User> list = this.getAllUser();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == id)
                return list.get(i);
        }
        return null;
    }

}
