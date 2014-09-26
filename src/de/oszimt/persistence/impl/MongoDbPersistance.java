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

    /**
     * Da MongoDbPersistance als Singleton fungiert haben
     * wir eine statische Variable um immer ein Zeiger auf
     * die Refferenze zu haben
     */
    private static MongoDbPersistance instance;

    /**
     * Der mongoClient ist das Hauptobject um den laufenden
     * MongoDb Service anzusprechen
     */
    private MongoClient mongoClient;

    /**
     * Da wir hier nur mit einer Datenbank arbeiten, können wir direkt
     * eine Refferenz auf die Schnittstelle speichern
     */
    private DB db;

    /**
     * Hier wird die Verbindung zur MongoDb aufgebaut
     * und die Datenbankrefference initalisiert
     *
     * @notice  Wir haben hier einen privaten Konstruktor da diese
     *          Klasse ein Singleton ist
     * @throws UnknownHostException
     */
    private MongoDbPersistance() throws UnknownHostException {
        mongoClient = new MongoClient( "localhost" , 27017 );
        this.db = mongoClient.getDB( "Usermanagement" );
    }

    /**
     * Um die Klasse auch als Singleton nutzen zu können haben wir
     * die statische Methode getInstance(). Sie erzeugt oder gibt eine
     * bestehende Refference der Klasse zurück
     * @return MongoDbPersistance
     * @throws UnknownHostException
     */
    public static MongoDbPersistance getInstance() throws UnknownHostException {
        if(instance == null){
            instance = new MongoDbPersistance();
        }
        return instance;
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Nummer zurück
     * @return String
     */
    public String getKeyUserId(){
       return "id";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Benutzervornamens zurück
     * @return String
     */
    public String getKeyUserFirstname(){
        return "first_name";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Benutzernachnamens zurück
     * @return String
     */
    public String getKeyUserLastname(){
        return "last_name";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Stadt zurück
     * @return String
     */
    public String getKeyUserCity(){
        return "city";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Strasse zurück
     * @return String
     */
    public String getKeyUserStreet(){
        return "street";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Strassen Nummer zurück
     * @return String
     */
    public String getKeyUserStreetNr(){
        return "street_nr";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer PLZ zurück
     * @return String
     */
    public String getKeyUserZipCode(){
        return "zip_code";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Benutzergeburtstags
     * @return String
     */
    public String getKeyUserBirthday(){
        return "birthday";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Abteilung zurück
     * @return String
     */
    public String getKeyUserDepartment(){
        return "department";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Abteilungsnummer zurück
     * @return String
     */
    public String getKeyDepartmentId(){
        return "id";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Abteilungsnames zurück
     * @return String
     */
    public String getKeyDepartmentName(){
        return "name";
    }

    /**
     * Um den Zugriff auf eine Collection zu vereinfachen wird die Methode
     * getCollection() mit aufgenommen
     *
     * @param name String
     * @return DBCollection
     */
    private DBCollection getCollection(String name){
        return this.db.getCollection(name);
    }

    /**
     * Kombination aus update/insert User. Wenn ein Benutzer, welcher
     * sich aus den Werten der Map ergibt, nicht existstiert dann wird er
     * neu erzeugt. Andererseits werden alle vorhandenen werde aus der Map in
     * das Dokument übernommen
     *
     * @param user Map<String,Object>
     */
    @Override
    public void upsertUser(Map<String,Object> user) {
        // versuchen den Benutzer zu laden um zu prüfen ob er existiert
        Map<String,Object> usr = this.getUserById((int)user.get(this.getKeyUserId()));
        //prüfen ob existiert
        if(usr != null){
            //konvertieren der Benutzer Map in ein BasicDBObject
            BasicDBObject doc = this.userMapToBasicDBObject(user);
            //erstellen des Such-Queries
            BasicDBObject search = new BasicDBObject(
                    this.getKeyUserId(),
                    user.get(this.getKeyUserId())
            );
            //Zeiger auf Benutzer-Collection laden
            DBCollection coll = this.getCollection("Users");
            //update an gefundenem Dokument durchführen
            coll.update(search,doc);
        }else{
            //erstellen eines neues Benutzers
            this.createUser(user);
        }
    }

    /**
     * Laden eines einzelnen Benutzers anhand seiner Id
     * @param id int
     * @return Map<String,Object>
     */
    public Map<String,Object> getUserById(int id){
        //leere Map inialisieren
        Map<String,Object> user = null;
        //zeiger auf Benutzer-Collection laden
        DBCollection coll = this.getCollection("Users");
        //Datenbankzeiger mittels Such-Query auf das Benutzer
        //Dokument zeigen lassen
        DBCursor cursor = coll.find(
                new BasicDBObject(
                        this.getKeyUserId(),
                        id
                )
        );
        //prüfen ob etwas gefunden wurde
        if(cursor.hasNext()){
            //erzeuge Benutzer Map anhand des Datenbank-Dokuments
            user = this.cursorNextToUserMap(cursor);
        }
        //zeiger löschen
        cursor.close();
        //Benutzer (oder wenn nicht vorhanden NULL) zurückgeben
        return user;
    }

    /**
     * Benutzer Dokument aus der Collection entfernen
     * @param id int
     */
    @Override
    public void deleteUser(int id) {
        //laden der Collection
        DBCollection coll = this.getCollection("Users");
        //Finden des Benutzer-Dokuments anhand eines Such-Queries
        DBObject doc = coll.findOne(new BasicDBObject(
                this.getKeyUserId(),
                id
        ));
        //Dokument aus der Collection entfernen
        coll.remove(doc);
    }

    /**
     * Konvertieren eine Benutzer Map zu einem BasicDBObject
     * BasicDBObject bilden die übliche JSON Struktur in MongoDb
     * nach.
     *
     * @param user Map<String,Object>
     * @return BasicDBObject
     */
    private BasicDBObject userMapToBasicDBObject(Map<String,Object> user){
        //leeres BasicDBObject erzeugen
        BasicDBObject doc = new BasicDBObject();
        //hinzufügen der Benutzerdaten unter Einbeziehung der festgelegten Key-Namen
        doc.append(this.getKeyUserId(), user.get(this.getKeyUserId()));
        doc.append(this.getKeyUserFirstname(), user.get(this.getKeyUserFirstname()));
        doc.append(this.getKeyUserLastname(), user.get(this.getKeyUserLastname()));
        doc.append(this.getKeyUserCity(), user.get(this.getKeyUserCity()));
        doc.append(this.getKeyUserStreet(), user.get(this.getKeyUserStreet()));
        doc.append(this.getKeyUserStreetNr(), user.get(this.getKeyUserStreetNr()));
        doc.append(this.getKeyUserZipCode(), user.get(this.getKeyUserZipCode()));
        //Konvertierung von LocalDate zu Date
        LocalDate ld = (LocalDate) user.get(this.getKeyUserBirthday());
        Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date res = Date.from(instant);
        doc.append(this.getKeyUserBirthday(), res);
        doc.append("department_id",((Map)user.get(this.getKeyUserDepartment())).get(this.getKeyDepartmentId()));
        return doc;
    }

    /**
     * Erstellt einen neuen Benutzer
     * @param user Map<String,Object>
     */
    @Override
    public void createUser(Map<String,Object> user) {
        //laden der Collection
        DBCollection coll = db.getCollection("Users");
        //finden der größten vorhandenen ID
        DBCursor cursor = coll.find().sort(new BasicDBObject(
                this.getKeyUserId(),-1
        ));
        int nextId = 0;

        //insofern Dokumente vorhanden
        if(cursor.count() > 0){
            nextId = Integer.parseInt(cursor.next().toMap().get(
                    this.getKeyUserId()
            ).toString());
            //id hochzählen
            nextId++;
        }

        //setzten/überschreiben der Benutzer ID durch die neue ID
        user.put(this.getKeyUserId(),nextId);
        //Benutzer Map zu einem BasicDBObject konvertieren
        BasicDBObject doc = this.userMapToBasicDBObject(user);
        //Dokument der Collection hinzufügen
        coll.insert(doc);
    }

    /**
     * Erstellen einer Benutzer Map mittel eines Datenbankzeigers
     *
     * @notice  Die Methode bewegt den Datenbankzeiger (cursor) eine Position
     *          nach vorn
     * @param cursor DBCursor
     * @return Map<String,Object>
     */
    private Map<String,Object> cursorNextToUserMap(DBCursor cursor){
        //Zeiger bewegen und Dokument zur Map konvertieren
        Map tmp = cursor.next().toMap();
        //laden der Abteilungs Colelction
        DBCollection coll = this.getCollection("Departments");

        BasicDBObject query = new BasicDBObject(
                this.getKeyDepartmentId(),
                Integer.parseInt(
                        tmp.get("department_id").toString()
                )
        );

        //finden der Abteilung anhand der in der Benutzer Map gegebenen Id
        DBObject depDBO = coll.findOne(query);

        //wurde was gefunden?
        if(depDBO != null) {
            //Abteilungs Dokument zu einen Map kovertieren
            Map depMap = depDBO.toMap();
            //hinzufügen der Abteilungs Map zut Benutzer Map
            tmp.put(this.getKeyUserDepartment(),depMap);
            return tmp;

        }
        return null;
    }

    /**
     * Gibt eine Liste alle Benutzer (Maps) zurück
     * @return List<Map<String,Object>>
     */
    @Override
    public List<Map<String,Object>> getAllUser() {
        //initalisieren einer leeren Liste
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //Laden der Benutzer Collection
        DBCollection coll = db.getCollection("Users");
        //Setzten des Datenbankzeigers an den Anfang der Collection
        DBCursor cursor = coll.find();
        try {
            //Dokumete iterieren
            while(cursor.hasNext()) {
                //Benutzer auf Zeiger zu Map konvertieren und der List anfügen
                list.add(this.cursorNextToUserMap(cursor));
            }
        } finally {
            //Zeiger löschen
            cursor.close();
        }
        return list;

    }

    /**
     * Anbeilungs Map zu BasicDBObject konvertieren
     * @param dep Map<String,Object>
     * @return BasicDBObject
     */
    private BasicDBObject departmentToBasicDBObject(Map<String,Object> dep){
        //leeres BasicDBObject initialisieren
        BasicDBObject doc = new BasicDBObject();
        //Werte aus Map, unter berücksichtigung der festgelegten keys, zuweisen
        doc.append(this.getKeyDepartmentId(), dep.get(this.getKeyDepartmentId()));
        doc.append(this.getKeyDepartmentName(),dep.get(this.getKeyDepartmentName()));
        return doc;
    }

    /**
     * Neue Abteilung erzeugen
     * @param name String
     *
     */
    @Override
    public void createDepartment(String name){
        //laden der Abteilungs Collection
        DBCollection coll = this.getCollection("Departments");
        //Ermitteln der größten id
        DBCursor cursor = coll.find().sort(new BasicDBObject(
                this.getKeyDepartmentId(), -1));
        int nextId = 1;
        //setzen der neuen id
        if(cursor.count() > 0){
            nextId = Integer.parseInt(cursor.next().toMap().get(
                    this.getKeyDepartmentId()
            ).toString());
            nextId++;
        }
        //erstellen einer Abteilungs map anhand der Id und dem Namen
        Map<String,Object> dep = new HashMap<String,Object>();
        dep.put(this.getKeyDepartmentId(), nextId);
        dep.put(this.getKeyDepartmentName(), name);
        //Abteilungs Map zu BasicDBObject umwandeln
        BasicDBObject doc = this.departmentToBasicDBObject(dep);
        //Abteilung der Collection hinzufügen
        coll.insert(doc);
    }

    /**
     * Aktualisieren einer Abteiung
     *
     * @param dep
     */
    @Override
    public void updateDepartment(Map<String, Object> dep){
        //laden der Abteilungs Collection
        DBCollection coll = this.getCollection("Departments");
        //Abteilungs Map umwandeln
        BasicDBObject doc = this.departmentToBasicDBObject(dep);
        //such Query inialisieren
        BasicDBObject serach = new BasicDBObject(
                this.getKeyDepartmentId(),dep.get(this.getKeyDepartmentId()));
        //Aktualisierung durchführen
        coll.update(serach,doc);
    }

    /**
     * Entfernen einer Abteilung
     *
     * @param id
     */
    @Override
    public void removeDepartment(int id){
        //laden der Abteilungs Collection
        DBCollection coll = this.getCollection("Departments");
        //Zulöschenes Dokument finden
        DBObject doc = coll.findOne(new BasicDBObject(
                this.getKeyDepartmentId(),id));
        //Dokument entfernen
        coll.remove(doc);
    }


    /**
     * Gibt eine Liste aller Abteilungen zurück
     *
     * @return
     */
    @Override
    public List<Map<String,Object>> getAllDepartments(){
        //leere Liste erzeugen
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //laden der Abteiungs Collection
        DBCollection coll = db.getCollection("Departments");
        //Zeiger auf Anfang der Collection setzten
        DBCursor cursor = coll.find();
        try {
            //Collection durchlaufen
            while(cursor.hasNext()) {
                //aktuelles Dokument in den Scope laden und Zeiger einen weiter bewegen
                DBObject tmp = cursor.next();
                //leere map für die Abteilung initialisieren
                Map<String, Object> map = new HashMap<String, Object>();
                //Abteilungs Map füllen
                map.put(this.getKeyDepartmentId(),Integer.parseInt(tmp.toMap().get(
                        this.getKeyDepartmentId()).toString())
                );
                map.put(this.getKeyDepartmentName(),tmp.toMap().get(
                        this.getKeyDepartmentName()
                ).toString());
                //Abteilungs map der Liste hinzufügen
                list.add(map);
            }
        } finally {
            cursor.close();
        }
        return list;
    }
}