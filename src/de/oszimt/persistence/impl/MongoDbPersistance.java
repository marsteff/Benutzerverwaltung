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
import org.omg.CORBA.BAD_CONTEXT;

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

    private DBCollection getCollection(String name){
        return this.db.getCollection(name);
    }

    public static MongoDbPersistance getInstance() throws UnknownHostException {
        if(instance == null){
            instance = new MongoDbPersistance();
        }
        return instance;
    }

    @Override
    public void upsertUser(User user) {

        User usr = this.getUserById(user.getId());
        if(usr != null){
            BasicDBObject doc = this.userToBasicDBObject(user);
            BasicDBObject search = new BasicDBObject("id",user.getId());
            DBCollection coll = this.getCollection("Users");
            coll.update(search,doc);
        }else{
            this.createUser(user);
        }
    }

    public User getUserById(int id){
        User user = null;
        DBCollection coll = db.getCollection("Users");
        DBCursor cursor = coll.find(new BasicDBObject("id",id));
        if(cursor.hasNext()){
            user = this.cursorNextToUser(cursor);
        }
        cursor.close();
        return user;
    }


    @Override
    public void deleteUser(User user) {
        DBCollection coll = this.getCollection("Users");
        DBObject doc = coll.findOne(new BasicDBObject("id",user.getId()));
        coll.remove(doc);
    }


    private BasicDBObject userToBasicDBObject(User user){
        BasicDBObject doc = new BasicDBObject();
        doc.append("id", user.getId());
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
        return doc;
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

        user.setId(nextId);
        BasicDBObject doc = this.userToBasicDBObject(user);
        coll.insert(doc);
    }

    private User cursorNextToUser(DBCursor cursor){
        Map tmp = cursor.next().toMap();
        return new User(
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
        );
    }

    @Override
    public List<User> getAllUser() {
        List<User> list = new ArrayList<User>();

        DBCollection coll = db.getCollection("Users");
        DBCursor cursor = coll.find();
        try {
            while(cursor.hasNext()) {
                list.add(this.cursorNextToUser(cursor));
            }
        } finally {
            cursor.close();
        }

        return list;

    }

    private BasicDBObject departmentToBasicDBObject(Department dep){
        BasicDBObject doc = new BasicDBObject();
        doc.append("id", dep.getId());
        doc.append("name",dep.getName());
        return doc;
    }

    @Override
    public void createDepartment(Department dep){
        DBCollection coll = this.getCollection("Departments");
        DBCursor cursor = coll.find().sort(new BasicDBObject("id", -1));
        int nextId = 0;

        if(cursor.count() > 0){
            nextId = Integer.parseInt(cursor.next().toMap().get("id").toString());
            nextId++;
        }

        dep.setId(nextId);

        BasicDBObject doc = this.departmentToBasicDBObject(dep);
        coll.insert(doc);
    }

    @Override
    public void updateDepartment(Department dep){
        DBCollection coll = this.getCollection("Departments");
        BasicDBObject doc = this.departmentToBasicDBObject(dep);
        BasicDBObject serach = new BasicDBObject("id",dep.getId());
        coll.update(serach,doc);
    }

    @Override
    public void remoteDepartment(Department dep){
        DBCollection coll = this.getCollection("Departments");
        DBObject doc = coll.findOne(new BasicDBObject("id",dep.getId()));
        coll.remove(doc);
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