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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class MongoDbPersistance implements IPersistance{

    private static MongoDbPersistance instance;

    private MongoClient mongoClient;
    private DB db;

    private MongoDbPersistance() throws UnknownHostException {
        mongoClient = new MongoClient( "localhost" , 27017 );
        this.db = mongoClient.getDB( "Usermanagement" );
    }

    public static MongoDbPersistance getInstance() throws UnknownHostException {
        if(instance == null){
            instance = new MongoDbPersistance();
        }
        return instance;
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
        DBCursor cursor = coll.find().sort(new BasicDBObject("id",-1));
        int nextId = 0;

        if(cursor.count() > 0){
            nextId = Integer.parseInt(cursor.next().toMap().get("id").toString());
            nextId++;
        }

        BasicDBObject doc = new BasicDBObject();
        doc.append("id", nextId);
        doc.append("first_name", user.getFirstname());
        doc.append("last_name", user.getLastname());
        doc.append("city", user.getCity());
        doc.append("street", user.getStreet());
        doc.append("street_nr", user.getStreetnr());
        doc.append("zip_code", user.getZipcode());
        LocalDate ld = user.getBirthday();
        Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date res = Date.from(instant);
        doc.append("birthday", res);
        doc.append("department_id",user.getDepartmentId());


        coll.insert(doc);
    }

    @Override
    public List<User> getAllUser() {
        List<User> list = new ArrayList<User>();

        DBCollection coll = db.getCollection("Users");
        DBCursor cursor = coll.find();
        try {
            while(cursor.hasNext()) {
                Map tmp = cursor.next().toMap();
                list.add(new User(
                        tmp.get("first_name").toString(),
                        tmp.get("last_name").toString(),
                        LocalDate.now(),//tmp.get("birthday"), "test"//@todo invalid date
                        tmp.get("city").toString(),
                        tmp.get("street").toString(),
                        tmp.get("street_nr").toString(),
                        Integer.parseInt(tmp.get("zip_code").toString()),
                        new Department(
                                Integer.parseInt(tmp.get("department_id").toString()),
                                "test"//@todo invalid name
                        )


                ));

            }
        } finally {
            cursor.close();
        }

        return list;

    }

    @Override
    public void createDepartment(Department dep){
        DBCollection coll = db.getCollection("Departments");
        DBCursor cursor = coll.find().sort(new BasicDBObject("id", -1));
        int nextId = 0;

        if(cursor.count() > 0){
            nextId = Integer.parseInt(cursor.next().toMap().get("id").toString());
            nextId++;
        }

        BasicDBObject doc = new BasicDBObject();
        doc.append("id", nextId);
        doc.append("name",dep.getName());

        coll.insert(doc);
    }

    @Override
    public void updateDepartment(Department dep){

    }

    @Override
    public void remoteDepartment(Department dep){

    }

    @Override
    public List<Department> getAllDepartments(){
        List<Department> list = new ArrayList<Department>();

        DBCollection coll = db.getCollection("Departments");
        DBCursor cursor = coll.find();
        try {
            while(cursor.hasNext()) {
                DBObject tmp = cursor.next();
                list.add(new Department(
                    Integer.parseInt(tmp.toMap().get("id").toString()),
                    tmp.toMap().get("name").toString()
                ));
            }
        } finally {
            cursor.close();
        }

        return list;
    }

}
