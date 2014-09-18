package de.oszimt.persistence.impl;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import javafx.collections.ObservableList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;

import java.net.UnknownHostException;
import java.util.List;


public class MongoDbPersistance implements IPersistance{

    private MongoClient mongoClient;
    private DB db;

    public MongoDbPersistance() throws UnknownHostException {
        mongoClient = new MongoClient( "localhost" , 27017 );
        this.db = mongoClient.getDB( "Usermanagement" );
    }

    public static MongoDbPersistance getInstance() throws UnknownHostException {
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
        DBCollection coll = db.getCollection("Users");
        BasicDBObject doc = new BasicDBObject();
        doc.append("id", user.getId());
        doc.append("first_name", user.getFirstname());
        doc.append("last_name", user.getLastname());
        doc.append("city", user.getCity());
        doc.append("street", user.getStreet());
        doc.append("street_nr", user.getStreetnr());
        doc.append("zip_code", user.getZipcode());
        doc.append("brithday", user.getBirthday());

        coll.insert(doc);
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
