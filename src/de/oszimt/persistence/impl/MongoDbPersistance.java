package de.oszimt.persistence.impl;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import javafx.beans.binding.MapBinding;
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
import javafx.collections.ObservableMap;
import org.omg.CORBA.BAD_CONTEXT;
import org.joda.time.DateTime;

import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


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

    public String getKeyUserId(){
       return "id";
    }
    public String getKeyUserFirstname(){
        return "first_name";
    }
    public String getKeyUserLastname(){
        return "last_name";
    }
    public String getKeyUserCity(){
        return "city";
    }
    public String getKeyUserStreet(){
        return "street";
    }
    public String getKeyUserStreetNr(){
        return "street_nr";
    }
    public String getKeyUserZipCode(){
        return "zip_code";
    }
    public String getKeyUserBirthday(){
        return "birthday";
    }
    public String getKeyUserDepartmentId(){
        return "department_id";
    }
    public String getKeyUserDepartment(){
        return "department";
    }

    public String getKeyDepartmentId(){
        return "id";
    }

    public String getKeyDepartmentName(){
        return "name";
    }

    private DBCollection getCollection(String name){
        return this.db.getCollection(name);
    }


    @Override
    public void upsertUser(Map<String,Object> user) {

        Map<String,Object> usr = this.getUserById((int)user.get(this.getKeyUserId()));
        if(usr != null){
            BasicDBObject doc = this.userMapToBasicDBObject(user);
            BasicDBObject search = new BasicDBObject(
                    this.getKeyUserId(),
                    user.get(this.getKeyUserId())
            );
            DBCollection coll = this.getCollection("Users");
            coll.update(search,doc);
        }else{
            this.createUser(user);
        }
    }

    public Map<String,Object> getUserById(int id){
        Map<String,Object> user = null;
        DBCollection coll = this.db.getCollection("Users");
        DBCursor cursor = coll.find(
                new BasicDBObject(
                        this.getKeyUserId(),
                        id
                )
        );
        if(cursor.hasNext()){
            user = this.cursorNextToUserMap(cursor);
        }
        cursor.close();
        return user;
    }


    @Override
    public void deleteUser(int id) {
        DBCollection coll = this.getCollection("Users");
        DBObject doc = coll.findOne(new BasicDBObject(
                this.getKeyUserId(),
                id
        ));
        coll.remove(doc);
    }


    private BasicDBObject userMapToBasicDBObject(Map<String,Object> user){
        BasicDBObject doc = new BasicDBObject();
        doc.append(this.getKeyUserId(), user.get(this.getKeyUserId()));
        doc.append(this.getKeyUserFirstname(), user.get(this.getKeyUserFirstname()));
        doc.append(this.getKeyUserLastname(), user.get(this.getKeyUserLastname()));
        doc.append(this.getKeyUserCity(), user.get(this.getKeyUserCity()));
        doc.append(this.getKeyUserStreet(), user.get(this.getKeyUserStreet()));
        doc.append(this.getKeyUserStreetNr(), user.get(this.getKeyUserStreetNr()));
        doc.append(this.getKeyUserZipCode(), user.get(this.getKeyUserZipCode()));
        LocalDate ld = (LocalDate) user.get(this.getKeyUserBirthday());
        Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date res = Date.from(instant);
        doc.append(this.getKeyUserBirthday(), res);
        doc.append(this.getKeyUserDepartmentId(),user.get(this.getKeyUserDepartmentId()));
        return doc;
    }

    @Override
    public void createUser(Map<String,Object> user) {

        DBCollection coll = db.getCollection("Users");
        DBCursor cursor = coll.find().sort(new BasicDBObject(
                this.getKeyUserId(),-1));
        int nextId = 0;

        if(cursor.count() > 0){
            nextId = Integer.parseInt(cursor.next().toMap().get(
                    this.getKeyUserId()
            ).toString());
            nextId++;
        }

        user.put(this.getKeyUserId(),nextId);
        BasicDBObject doc = this.userMapToBasicDBObject(user);
        coll.insert(doc);
    }

    private Map<String,Object> cursorNextToUserMap(DBCursor cursor){
        Map tmp = cursor.next().toMap();

        DBCollection coll = this.getCollection("Departments");
        DBObject depDBO = coll.findOne(new BasicDBObject(
                this.getKeyUserId()
                ,Integer.parseInt(
                tmp.get(this.getKeyUserDepartmentId()).toString()
        )));

        if(depDBO != null) {
            Map depMap = depDBO.toMap();
            tmp.put(this.getKeyUserDepartment(),depMap);
            return tmp;

        }
        return null;
    }

    @Override
    public List<Map<String,Object>> getAllUser() {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        DBCollection coll = db.getCollection("Users");
        DBCursor cursor = coll.find();
        try {
            while(cursor.hasNext()) {
                list.add(this.cursorNextToUserMap(cursor));
            }
        } finally {
            cursor.close();
        }

        return list;

    }

    private BasicDBObject departmentToBasicDBObject(Map<String,Object> dep){
        BasicDBObject doc = new BasicDBObject();
        doc.append(this.getKeyDepartmentId(), dep.get(this.getKeyDepartmentId()));
        doc.append(this.getKeyDepartmentName(),dep.get(this.getKeyDepartmentName()));
        return doc;
    }

    @Override
    public void createDepartment(String name){
        DBCollection coll = this.getCollection("Departments");
        DBCursor cursor = coll.find().sort(new BasicDBObject(
                this.getKeyDepartmentId(), -1));
        int nextId = 1;

        if(cursor.count() > 0){
            nextId = Integer.parseInt(cursor.next().toMap().get(
                    this.getKeyDepartmentId()
            ).toString());
            nextId++;
        }

        Map<String,Object> dep = new HashMap<String,Object>();
        dep.put(this.getKeyDepartmentId(), nextId);
        dep.put(this.getKeyDepartmentName(), name);

        BasicDBObject doc = this.departmentToBasicDBObject(dep);
        coll.insert(doc);
    }

    @Override
    public void updateDepartment(Map<String, Object> dep){
        DBCollection coll = this.getCollection("Departments");
        BasicDBObject doc = this.departmentToBasicDBObject(dep);
        BasicDBObject serach = new BasicDBObject(
                this.getKeyDepartmentId(),dep.get(this.getKeyDepartmentId()));
        coll.update(serach,doc);
    }

    @Override
    public void removeDepartment(int id){
        DBCollection coll = this.getCollection("Departments");
        DBObject doc = coll.findOne(new BasicDBObject(
                this.getKeyDepartmentId(),id));
        coll.remove(doc);
    }


    @Override
    public List<Map<String,Object>> getAllDepartments(){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        DBCollection coll = db.getCollection("Departments");
        DBCursor cursor = coll.find();
        try {
            while(cursor.hasNext()) {
                DBObject tmp = cursor.next();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(this.getKeyDepartmentId(),Integer.parseInt(tmp.toMap().get(
                        this.getKeyDepartmentId()
                ).toString()));
                map.put(this.getKeyDepartmentName(),tmp.toMap().get(
                        this.getKeyDepartmentName()
                ).toString());
                list.add(map);
            }
        } finally {
            cursor.close();
        }

        return list;
    }

}