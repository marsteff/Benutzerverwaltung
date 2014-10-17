package de.oszimt.concept.impl;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.util.Util;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class Concept implements IConcept {

    /**
     * Titel der Anwendung
     */
    private String title = "Oszimt Projekt Benutzerverwaltung";

    /**
     * Datenhaltung
     */
    private IPersistance database;

    /**
     * Konstruktor, bekommnt eine Datenhaltungs Klasse
     * übergeben
     *
     * @param db
     */
    public Concept(IPersistance db){
        this.database = db;

        //Prüfen, ob Departments vorhanden sind, und wenn nicht, erzeugen
        List<Department> departmentList = this.getAllDepartments();
        if(departmentList.size() == 0){
            Util.createDepartments(this);
        }
    }

    /**
     * Gibt den Title der Anwendung zurück
     * @return
     */
    @Override
    public String getTitle(){
        return this.title;
    }

    /**
     * Löscht einen Benutzer aus der Datenhaltung
     *
     * @param user
     * @return
     */
    @Override
    public void deleteUser(User user){
        this.database.deleteUser(user.getId());
    }

    /**
     * Die Datenhaltungschicht gibt Ihre Resultate immer als
     * Map zurück. Hier wird die Map, welche Benutzerinformationen
     * trägt in ein Benutzer Object konvertiert. Die Bezeichungen der
     * einzelnen Felder sind in der Datenhaltung festgelegt und können
     * über die getKey..() Methoden abgefragt werden.
     *
     * @param userMap
     * @return
     */
    private User userMapToUser(Map<String,Object> userMap){

        //Auf gpültigkeit prüfen
        if(userMap == null){
            return null;
        }

        //Datenhaltungsreffernce in keys speicher (erspart schreibarbeit)
        IPersistance keys = this.database;
        //initialisieren der PLZ
        int zipCode = 0;
        if(userMap.containsKey(keys.getKeyUserZipCode())){
            zipCode = Integer.parseInt(
                    userMap.get(keys.getKeyUserZipCode()).toString()
            );
        }
        //initialisieren des Geburtstages
        LocalDate birthday = userMap.containsKey(keys.getKeyUserBirthday()) ?
                this.DateToLocalDate((Date) userMap.get(
                        keys.getKeyUserBirthday())
                ) : null;

        //Neuen Benutzer anlegen und mittels des Konstruktor initalisieren
        //containsKey() Abfragen sollen NullPointerExceptions verhindern
        User user = new User(
                this.readSaveFromMap(userMap,keys.getKeyUserFirstname()),
                this.readSaveFromMap(userMap, keys.getKeyUserLastname()),
                birthday,
                this.readSaveFromMap(userMap,keys.getKeyUserCity()),
                this.readSaveFromMap(userMap,keys.getKeyUserStreet()),
                this.readSaveFromMap(userMap,keys.getKeyUserStreetNr()),
                zipCode,
                new Department(
                        (int) ((Map) userMap.get(
                                keys.getKeyUserDepartment())
                        ).get(keys.getKeyDepartmentId()),
                        ((Map) userMap.get(
                                keys.getKeyUserDepartment())
                        ).get(keys.getKeyDepartmentName()).toString()
                )
        );
        user.setId(userMap.containsKey(keys.getKeyUserId()) ?
                (int) userMap.get(keys.getKeyUserId()) : 0);
        return user;
    }

    /**
     * Umwandung von LocalDate zu Date
     *
     * @param date
     * @return
     */
    private LocalDate DateToLocalDate(Date date){
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date.getTime()),
                ZoneId.systemDefault()
        ).toLocalDate();
    }

    /**
     * Gibt den Wert einer Map zurück
     * Prüft ob der gegebene Key existsiert.
     * @notice Gibt bei fehlendem Key NULL zurück
     *
     * @param map
     * @param key
     * @return
     */
    private String readSaveFromMap(Map map, String key){
        return map.containsKey(key) ? map.get(key).toString() : null;
    }

    /**
     * Für die Datenhaltungsschicht werden alle Objecte in Form
     * einer Map mit den ensprechenden Werten benötigt. Hier
     * wir ein Department Object in die entsprechende Map umgewandelt
     *
     * @param dep
     * @return
     */
    private Map<String,Object> departmentToDepMap(Department dep){
        //erstellen einer Map, hier Hashmap um put nutzen zu können
        Map<String, Object> depMap = new HashMap<String, Object>();
        //hinzufügen der Werte mittel, in der Datenhaltundschicht,
        //definierten Keys
        depMap.put(this.database.getKeyDepartmentId(),dep.getId());
        depMap.put(this.database.getKeyDepartmentName(),dep.getName());
        return depMap;
    }

    /**
     * Die Datenhaltungschicht gibt Ihre Resultate immer als
     * Map zurück. Hier wird die Map, einer Abteilung in ein
     * Department Object konvertiert.
     *
     * @param depMap
     * @return
     */
    private Department depMapToDepartment(Map<String,Object> depMap){
        Department dep = new Department(depMap.get(this.database.getKeyDepartmentName()).toString());
        dep.setId((int)depMap.get(this.database.getKeyDepartmentId()));
        return dep;
    }

    /**
     * Für die Datenhaltungsschicht werden alle Objecte in Form
     * einer Map mit den ensprechenden Werten benötigt. Hier
     * wir ein Benutzer-Object in eine solche Map umgewandelt.
     *
     * @param user
     * @return
     */
    private Map<String,Object> userToUserMap(User user){
        //Datenhaltungsreffernce in keys speicher (erspart schreibarbeit)
        IPersistance keys = this.database;
        //erstellen einer Map, hier Hashmap um put nutzen zu können
        Map<String, Object> userMap = new HashMap<String, Object>();
        //map für Abteilung erstellen
        Map<String, Object> depMap = this.departmentToDepMap(
                user.getDepartment()
        );
        //hinzufügen der Werte mittel, in der Datenhaltundschicht,
        //definierten Keys
        userMap.put(keys.getKeyUserId(),user.getId());
        userMap.put(keys.getKeyUserFirstname(),user.getFirstname());
        userMap.put(keys.getKeyUserLastname(),user.getLastname());
        userMap.put(keys.getKeyUserBirthday(),user.getBirthday());
        userMap.put(keys.getKeyUserCity(),user.getCity());
        userMap.put(keys.getKeyUserStreet(),user.getStreet());
        userMap.put(keys.getKeyUserStreetNr(),user.getStreetnr());
        userMap.put(keys.getKeyUserZipCode(),user.getZipcode());
        userMap.put(keys.getKeyUserDepartment(),depMap);

        return userMap;
    }

    /**
     * Erstellt einen Neuen User
     * Benutzer Objekt wird in eine Map umgewandelt und dann
     * zu speichern an die Datenhaltungschicht weitergegeben
     *
     * @param user
     */
    @Override
    public void createUser(User user){
        this.database.createUser(this.userToUserMap(user));
    }

    /**
     * Erstellt einen Benutzer insofern er noch nicht existsiert, andererseits werden
     * die Benutzerdetails aktualisiert
     *
     * @param user
     */
    @Override
    public void upsertUser(User user){
        this.database.upsertUser(this.userToUserMap(user));
    }

    /**
     * Erstellen von Zufall-Benutzern
     * Zu Test- und Demonstrationszwecken können zufällige Benutzer
     * angelegt werden.
     *
     * @notice Bei Benutzung REST Paramter muss Internet vorhanden sein.
     * Hier wird eine realistische Zuordnung von PLZ zu Stadt generiert.
     * Das Abfragen der Städte geschieht mittels eines REST Services
     *
     * @param useRest
     */
    @Override
    public void createRandomUsers(boolean useRest){
        //Die Funktionalität wird in Util ausgelagert
        //da Sie im eigentlichen Sinne kein Teil der
        //Anwendung ist
        Util.createCustomers(useRest,this);
    }

    /**
     * Erstllen einer neuen Abteilung
     * wird an die Datenhaltung weitergeleited
     *
     * @param name
     */
    @Override
    public void createDepartment(String name){
        this.database.createDepartment(name);
    }

    @Override
    public void upsertDepartment(Department department) {
        this.database.upsertDepartment(this.departmentToDepMap(department));
    }

    @Override
    public void deleteDepartment(Department department) {
        this.database.deleteDepartment(department.getId());
    }

    /**
     * Gibt eine Liste aller Abteilungen zurück
     *
     * @return
     */
    @Override
    public List<Department> getAllDepartments(){
        //List mit Maps aus Datenhaltung empfangen und alle Elemente in Department Objekte umwandeln
        return this.database.getAllDepartments().stream().map(this::depMapToDepartment).collect(Collectors.toList());
    }

    /**
     * Gibt eine Liste aller Benutzer zurück
     *
     * @return
     */
    @Override
    public List<User> getAllUser(){
        //List mit Maps aus Datenhaltung empfangen und alle Elemente in User Objekte umwandeln
        return this.database.getAllUser().stream().map(this::userMapToUser).collect(Collectors.toList());
    }


    /**
     * Gibt einen einzlnen Benutzer anhand seiner ID zurück
     *
     * @param id
     * @return
     */
    @Override
    public User getUser(int id) {
        //Hole den Benutzer aus der Datenhaltung und konvertiere die Map
        //zu einem User Object
        return this.userMapToUser(this.database.getUserById(id));
    }
}
