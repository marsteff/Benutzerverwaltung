package de.oszimt.concept.impl;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.util.Util;

import javafx.collections.ObservableList;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
                userMap.containsKey(keys.getKeyUserBirthday()) ?
                        LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(
                                ((Date) userMap.get(keys.getKeyUserBirthday())).getTime()
                        ),
                        ZoneId.systemDefault()).toLocalDate() : null,
                userMap.containsKey(keys.getKeyUserCity()) ? userMap.get(keys.getKeyUserCity()).toString() : null,
                userMap.containsKey(keys.getKeyUserStreet()) ? userMap.get(keys.getKeyUserStreet()).toString() : null,
                userMap.containsKey(keys.getKeyUserStreetNr()) ? userMap.get(keys.getKeyUserStreetNr()).toString() : null,
                userMap.containsKey(keys.getKeyUserZipCode()) ? Integer.parseInt(userMap.get(keys.getKeyUserZipCode()).toString()) : null,
                new Department(
                        (int) ((Map) userMap.get(keys.getKeyUserDepartment())).get(keys.getKeyDepartmentId()),
                        ((Map) userMap.get(keys.getKeyUserDepartment())).get(keys.getKeyDepartmentName()).toString()
                )
        );
    }

    private Map<String,Object> departmentToDepMap(Department dep){
        Map<String, Object> depMap = new HashMap<String, Object>();
        depMap.put(this.database.getKeyDepartmentId(),dep.getId());
        depMap.put(this.database.getKeyDepartmentName(),dep.getName());
        return depMap;
    }

    private Department depMapToDepartment(Map<String,Object> depMap){
        Department dep = new Department(depMap.get(this.database.getKeyDepartmentName()).toString());
        dep.setId((int)depMap.get(this.database.getKeyDepartmentId()));
        return dep;
    }

    private Map<String,Object> userToUserMap(User user){
        Map<String, Object> userMap = new HashMap<String, Object>();
        Map<String, Object> depMap = new HashMap<String, Object>();
        IPersistance keys = this.database;

        depMap.put(keys.getKeyDepartmentId(),user.getDepartmentId());
        depMap.put(keys.getKeyDepartmentName(),user.getDepartment());

        userMap.put(keys.getKeyUserId(),user.getId());
        userMap.put(keys.getKeyUserFirstname(),user.getFirstname());
        userMap.put(keys.getKeyUserLastname(),user.getLastname());
        userMap.put(keys.getKeyUserBirthday(),user.getBirthday());
        userMap.put(keys.getKeyUserCity(),user.getCity());
        userMap.put(keys.getKeyUserStreet(),user.getStreet());
        userMap.put(keys.getKeyUserStreetNr(),user.getStreetnr());
        userMap.put(keys.getKeyUserZipCode(),user.getZipcode());
        userMap.put(keys.getKeyUserDepartmentId(),user.getDepartmentId());
        userMap.put(keys.getKeyUserDepartment(),depMap);

        return userMap;
    }

    @Override
    public boolean createUser(User user){
        this.database.createUser(userToUserMap(user));
        return true;
    }

    @Override
    public boolean upsertUser(User user){
        this.database.upsertUser(userToUserMap(user));
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
            this.database.createDepartment(dep);
        }

        List<Department> list = this.getAllDepartments();

        ObservableList<User> customerList = Util.createCustomers(useRest, list);
        for(User user : customerList){
            this.createUser(user);
        }
    }

    @Override
    public List<Department> getAllDepartments(){
        return this.database.getAllDepartments().stream().map(this::depMapToDepartment).collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUser(){
        return this.database.getAllUser().stream().map(this::userMapToUser).collect(Collectors.toList());
    }


    @Override
    public User getUser(int id) {
        return this.userMapToUser(this.database.getUserById(id));
    }

}
