package de.oszimt.persistence.impl;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import javafx.collections.ObservableList;

import java.util.List;


public class MongoDbPersistance implements IPersistance{
    public MongoDbPersistance(){

    }

    public static MongoDbPersistance getInstance(){
        return new MongoDbPersistance();
    }

    @Override
    public void upsertUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void createUser(User user) {

    }

    @Override
    public ObservableList<User> getAllUser() {
        return null;
    }

    @Override
    public void createDepartment(Department dep){

    }

    @Override
    public void updateDepartment(Department dep){

    }

    @Override
    public void remoteDepartment(Department dep){

    }

    @Override
    public List<Department> getAllDepartments(){
        return null;
    }

}
