package de.oszimt.persistence.impl;

import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import javafx.collections.ObservableList;


public class MongoDbPersistance implements IPersistance{
    public MongoDbPersistance(){

    }

    public static MongoDbPersistance getInstance(){
        return new MongoDbPersistance();
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void createUser(User user) {

    }

    @Override
    public ObservableList<User> getAllKunden() {
        return null;
    }
}
