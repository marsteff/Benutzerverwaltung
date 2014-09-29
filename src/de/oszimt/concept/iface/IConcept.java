package de.oszimt.concept.iface;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;

import java.util.List;

/**
 * Created by Marci on 18.09.2014.
 */
public interface IConcept {
    /**
     * Gibt den Title der Anwendung zurück
     * @return
     */
    String getTitle();

    /**
     * Löscht einen Benutzer aus der Datenhaltung
     *
     * @param user
     * @return
     */
    void deleteUser(User user);

    /**
     * Erstellt einen Neuen User
     * Benutzer Objekt wird in eine Map umgewandelt und dann
     * zu speichern an die Datenhaltungschicht weitergegeben
     *
     * @param user
     */
    void createUser(User user);

    /**
     * Erstellt einen Benutzer insofern er noch nicht existsiert, andererseits werden
     * die Benutzerdetails aktualisiert
     *
     * @param user
     */
    void upsertUser(User user);

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
    void createRandomUsers(boolean useRest);

    /**
     * Erstllen einer neuen Abteilung
     * wird an die Datenhaltung weitergeleited
     *
     * @param name
     */
    void createDepartment(String name);

    /**
     * Gibt eine Liste aller Abteilungen zurück
     *
     * @return
     */
    List<Department> getAllDepartments();

    /**
     * Gibt eine Liste aller Benutzer zurück
     *
     * @return
     */
    List<User> getAllUser();

    /**
     * Gibt einen einzlnen Benutzer anhand seiner ID zurück
     *
     * @param id
     * @return
     */
    User getUser(int id);
}
