package de.oszimt.concept.impl;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.util.Util;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Concept implements IConcept {

    /**
     * Titel der Anwendung
     */
    private String title = "Oszimt Projekt Benutzerverwaltung";

    /**
     * Datenhaltung
     */
    private IPersistance db;

    /**
     * Konstruktor, bekommnt eine Datenhaltungs Klasse
     * übergeben
     *
     * @param db
     */
    public Concept(IPersistance db){
        this.db = db;
        //Prüfen, ob Departments vorhanden sind, und wenn nicht, erzeugen
        if(this.getAllDepartments().size() < 1){
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
        this.db.deleteUser(user.getId());
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
        //Auf gültigkeit prüfen
        if(userMap == null){
            return null;
        }

        //initialisieren der PLZ
        int zipCode = Util.readSaveFromMap(userMap, db.getKeyUserZipCode());

        //initialisieren des Geburtstages
        Date bday = Util.readSaveFromMap(userMap, db.getKeyUserBirthday());
        LocalDate birthday = Util.DateToLocalDate(bday);

        //initialisieren der Abteilungs Map
        Map department = Util.readSaveFromMap(userMap, db.getKeyUserDepartment());

        //Neuen Benutzer anlegen und mittels des Konstruktor initalisieren
        //containsKey() Abfragen sollen NullPointerExceptions verhindern
        User user = new User(
                Util.readSaveFromMap(userMap, db.getKeyUserFirstname()),
                Util.readSaveFromMap(userMap, db.getKeyUserLastname()),
                birthday,
                Util.readSaveFromMap(userMap, db.getKeyUserCity()),
                Util.readSaveFromMap(userMap, db.getKeyUserStreet()),
                Util.readSaveFromMap(userMap, db.getKeyUserStreetNr()),
                zipCode,
                new Department(
                        Util.readSaveFromMap(department, db.getKeyDepartmentId()),
                        Util.readSaveFromMap(department, db.getKeyDepartmentName())
                )
        );
        //setzen der Benutzer ID
        user.setId(Util.readSaveFromMap(userMap, db.getKeyUserId()));
        return user;
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
        Map<String, Object> depMap = new HashMap<>();
        //hinzufügen der Werte mittel, in der Datenhaltundschicht,
        //definierten Keys
        depMap.put(this.db.getKeyDepartmentId(),dep.getId());
        depMap.put(this.db.getKeyDepartmentName(),dep.getName());
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
        Department dep = new Department(
                Util.readSaveFromMap(depMap, db.getKeyDepartmentId()),
                Util.readSaveFromMap(depMap, db.getKeyDepartmentName()),
                Util.readSaveFromMap(depMap, db.getKeyDepartmentAmount())
        );
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
        IPersistance keys = this.db;
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
        this.db.createUser(this.userToUserMap(user));
    }

    /**
     * Erstellt einen Benutzer insofern er noch nicht existsiert, andererseits werden
     * die Benutzerdetails aktualisiert
     *
     * @param user
     */
    @Override
    public void upsertUser(User user){
        this.db.upsertUser(this.userToUserMap(user));
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
        this.db.createDepartment(name);
    }

    @Override
    public void upsertDepartment(Department department) {
        this.db.upsertDepartment(this.departmentToDepMap(department));
    }

    @Override
    public Department getDepartmentById(int id){
        return depMapToDepartment(this.db.getDepartmentById(id));
    }

    @Override
    public List<User> getUsersByDepartment(Department dep) {
        return this.db.getUsersByDepartmentId(dep.getId()).stream().map(this::userMapToUser).collect(Collectors.toList());
    }

    @Override
    public void deleteDepartment(Department department) {
        this.db.deleteDepartment(department.getId());
    }

    /**
     * Gibt eine Liste aller Abteilungen zurück
     *
     * @return
     */
    @Override
    public List<Department> getAllDepartments(){
        //List mit Maps aus Datenhaltung empfangen und alle Elemente in Department Objekte umwandeln
        return this.db.getAllDepartments().stream().map(this::depMapToDepartment).collect(Collectors.toList());
    }

    /**
     * Gibt eine Liste aller Benutzer zurück
     *
     * @return
     */
    @Override
    public List<User> getAllUser(){
        //List mit Maps aus Datenhaltung empfangen und alle Elemente in User Objekte umwandeln
        return this.db.getAllUser().stream().map(this::userMapToUser).collect(Collectors.toList());
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
        return this.userMapToUser(this.db.getUserById(id));
    }
}
