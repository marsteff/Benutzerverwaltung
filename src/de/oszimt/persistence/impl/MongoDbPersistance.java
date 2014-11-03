package de.oszimt.persistence.impl;

import com.mongodb.*;
import de.oszimt.persistence.iface.IPersistance;

import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Datenhaltung for MongoDB
 * getestet für MongoDB shell version: 2.6.3+
 *
 * Implementierung des IPersistance Interfaces für die
 * Dokumenten orientierte Datenbank MongoDB
 */
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
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Abteilungsnames zurück
     * @return String
     */
    public String getKeyDepartmentAmount(){
        return "amount";
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
     * @param user Map mit Benutzer Daten
     */
    @Override
    public void upsertUser(Map<String,Object> user) {
        //versuchen den Benutzer zu laden um zu prüfen ob er existiert
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
     * @param id Id des Benurtzers
     * @return Map mit Daten des Benutzers
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
     * Laden einer einzelnen Abteilung anhand der Id
     * @param id Id der Abteilung
     * @return Map mit Daten der Abteilung
     */
    @Override
    public Map<String,Object> getDepartmentById(int id){
        //leere Map inialisieren
        Map<String,Object> dep = null;
        //zeiger auf Abteilungs-Collection
        DBCollection coll = this.getCollection("Departments10");
        //Datenbankzeiger mittels Such-Query auf das Abteilungs
        //Dokument zeigen lassen
        DBCursor cursor = coll.find(
                new BasicDBObject(
                        this.getKeyDepartmentId(),
                        id
                )
        );
        //prüfen ob etwas gefunden wurde
        if(cursor.hasNext()){
            //erzeuge Benutzer Map anhand des Datenbank-Dokuments
            dep = this.cursorNextToDepartmentMap(cursor);
        }
        //zeiger löschen
        cursor.close();
        //Benutzer (oder wenn nicht vorhanden NULL) zurückgeben
        return dep;
    }

    /**
     * Benutzer Dokument aus der Collection entfernen
     * @param id Id des zulöschenden Benutzers
     */
    @Override
    public void deleteUser(int id) {
        //laden der Collection
        DBCollection coll = this.getCollection("Users");
        //Finden des Benutzer-Dokuments anhand eines Such-Queries
        DBObject doc = coll.findOne(new BasicDBObject(this.getKeyUserId(),id));
        if(doc != null) {
            //Dokument aus der Collection entfernen
            coll.remove(doc);
        }
    }

    /**
     * Konvertieren eine Benutzer Map zu einem BasicDBObject
     * BasicDBObject bilden die übliche JSON Struktur in MongoDb
     * nach.
     *
     * @param user Map mit Daten des Benzuters
     * @return BasicDBObject
     */
    private BasicDBObject userMapToBasicDBObject(Map<String,Object> user){
        //leeres BasicDBObject erzeugen
        BasicDBObject doc = new BasicDBObject();
        //hinzufügen der Benutzerdaten unter Einbeziehung der festgt()));
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
     * @param user Map mit Benutzer Daten
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
     * @return Map mit Daten des Benutzers
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
     * @return Liste von Maps mit Daten aller Benutzer
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
     * Gibt eine Liste alle Benutzer (Maps) die einer Abteilungs IDs zugeordnet sind, zurück
     * @return Liste von Maps mit Daten aller Abteilungen
     */
    @Override
    public List<Map<String, Object>> getUsersByDepartmentId(int id) {
        //leere Map List inialisieren
        List<Map<String, Object>> users = new ArrayList<>();
        //zeiger auf Benutzer-Collection laden
        DBCollection coll = this.getCollection("Users");
        //Datenbankzeiger mittels Such-Query auf das Benutzer
        //Dokument zeigen lassen
        DBCursor cursor = coll.find(
                new BasicDBObject(
                        "department_id",
                        id
                )
        );
        //prüfen ob etwas gefunden wurde
        while(cursor.hasNext()){
            //erzeuge Benutzer Map anhand des Datenbank-Dokuments und
            //füge es der Liste hinzu
            users.add(this.cursorNextToUserMap(cursor));
        }
        //zeiger löschen
        cursor.close();
        //Benutzer Liste (oder wenn nicht vorhanden NULL) zurückgeben
        return users.size() == 0 ? null : users;
    }

    /**
     * Anbeilungs Map zu BasicDBObject konvertieren
     * @param dep Map mit Daten einer Abteilung
     * @return BasicDBObject JSON ersatz in Java
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
     * @param name Name der zuerstellenden Abteilung
     */
    @Override
    public void createDepartment(String name){
        //Laden der Abteilungs Collection
        DBCollection coll = this.getCollection("Departments");
        //ermitteln der größten Id
        DBCursor cursor = coll.find().sort(new BasicDBObject(this.getKeyDepartmentId(), -1));
        int nextId = 1;
        //setzen der neuen id
        if(cursor.count() > 0){
            nextId = Integer.parseInt(cursor.next().toMap().get(
                    this.getKeyDepartmentId()
            ).toString());
            nextId++;
        }
        //neue Map für Abteilungs Daten initialisieren
        Map<String,Object> dep = new HashMap<String,Object>();
        //hinzufügen der Abteilungs daten
        dep.put(this.getKeyDepartmentId(), nextId);
        dep.put(this.getKeyDepartmentName(), name);
        //Map zu BasicDBObject konvertieren
        BasicDBObject doc = this.departmentToBasicDBObject(dep);
        //Dokument der Collection hinzufügen
        coll.insert(doc);
    }

    /**
     * Ändern einer Abteilung
     * @param dep Map mit Daten einer Abteilung
     */
    @Override
    public void upsertDepartment(Map<String, Object> dep){
        //versuchen die Abteilung zu laden um zu prüfen ob sie existiert
        Map<String,Object> d = this.getDepartmentById((int)dep.get(this.getKeyDepartmentId()));
        //prüfen ob existiert
        if(d != null){
            //konvertieren der Abteilungs Map in ein BasicDBObject
            BasicDBObject doc = this.departmentToBasicDBObject(dep);
            //erstellen des Such-Queries
            BasicDBObject search = new BasicDBObject(
                    this.getKeyDepartmentId(),
                    dep.get(this.getKeyDepartmentId())
            );
            //Zeiger auf Abteilungs-Collection laden
            DBCollection coll = this.getCollection("Departments");
            //update an gefundenem Dokument durchführen
            coll.update(search,doc);
        }else{
            //erstellen eines neues Benutzers
            this.createDepartment(dep.get(this.getKeyDepartmentName()) + "");
        }
    }

    /**
     * Löschen einer Abteilung
     * @param id Id der zulöschenden Abteilung
     */
    @Override
    public void deleteDepartment(int id){
        //Laden der Abteilungs Collection
        DBCollection coll = this.getCollection("Departments");
        //Dokument anhand der Id finden
        DBObject doc = coll.findOne(new BasicDBObject(this.getKeyDepartmentId(),id));
        //Dokument aus der Collection entfernen
        coll.remove(doc);
    }

    /**
     * Erstellen einer Abteilungs Map mittel eines Datenbankzeigers
     *
     * @notice  Die Methode bewegt den Datenbankzeiger (cursor) eine Position
     *          nach vorn
     * @param cursor DBCursor
     * @return Map mit Daten der Abteilung
     */
    private Map<String,Object> cursorNextToDepartmentMap(DBCursor cursor){
        //aktuelles Dokument in den Scope laden und Zeiger einen weiter bewegen
        DBObject tmp = cursor.next();
        //leere map für die Abteilung initialisieren
        Map<String, Object> map = new HashMap<>();
        //Abteilungs id
        int id = Integer.parseInt(tmp.toMap().get(this.getKeyDepartmentId()).toString());
        //Abteilungs Map füllen
        map.put(this.getKeyDepartmentId(),id);
        map.put(this.getKeyDepartmentName(),tmp.toMap().get(
                this.getKeyDepartmentName()
        ).toString());
        //Anzahl der Benutzer in dieser Abteilung ermitten
        int amount = getUserAmount(id);
        //Anzahl der Map hinzufügen
        map.put(this.getKeyDepartmentAmount(),amount);
        return map;
    }

    /**
     * Gibt eine Liste aller Abteilungen zurück
     * @return Liste von Maps mit Daten aller Abteilungen
     */
    @Override
    public List<Map<String,Object>> getAllDepartments(){
        //leere Liste erzeugen
        List<Map<String,Object>> list = new ArrayList<>();
        //laden der Abteiungs Collection
        DBCollection coll = db.getCollection("Departments");
        //Zeiger auf Anfang der Collection setzten
        DBCursor cursor = coll.find();
        try {
            //Collection durchlaufen
            while(cursor.hasNext()) {
                //Abteilungs map der Liste hinzufügen
                list.add(cursorNextToDepartmentMap(cursor));
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    /**
     * Gibt die Anzahl der Benutzer in einer Abteilung
     * @param department_id Id der Abteilung
     * @return Anzahl der Benutzer in der Abteilung
     */
    private int getUserAmount(int department_id){
        //Laden der Benutzer Collection
        DBCollection coll = db.getCollection("Users");
        //Suche auf Abteilungs ID begenzen
        BasicDBObject query = new BasicDBObject("department_id", department_id);
        //suchen + zählen
        return (int) coll.count(query);
    }
}